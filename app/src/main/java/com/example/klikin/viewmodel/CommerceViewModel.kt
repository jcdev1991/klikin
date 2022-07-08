package com.example.klikin.viewmodel

import android.util.Log
import androidx.core.util.Pair
import androidx.lifecycle.MutableLiveData
import com.android.volley.VolleyError
import com.example.klikin.util.base.BaseViewModel
import com.example.klikin.util.volley.VolleyArrayCallback
import com.example.klikin.util.volley.VolleyErrorRequest
import com.example.klikin.model.Commerce
import com.example.klikin.repository.CommerceRepository
import org.json.JSONArray

class CommerceViewModel : BaseViewModel() {

    val repositoryCommerce = CommerceRepository.getInstance()
    val checkCommercesiveData: MutableLiveData<List<Commerce>> = MutableLiveData<List<Commerce>>()

    fun getCommerces(minPage: Int, maxPage: Int) {
        repositoryCommerce!!.getCommerces(object : VolleyArrayCallback {

            override fun onResponse(response: Pair<JSONArray, MutableMap<String, String>>) {
                val jsonArray = response?.first
                val arrayList = ArrayList<Commerce>()
                for (i in minPage until maxPage) {
                    arrayList.add(gson.fromJson(jsonArray.get(i).toString(), Commerce::class.java))
                }
                checkCommercesiveData.value = arrayList
            }

            override fun onError(error: VolleyError?) {
                if (error?.networkResponse?.data != null) {
                    Log.e("ERROR", String(error.networkResponse.data))
                }
                checkCommercesiveData.value = ArrayList()
                VolleyErrorRequest.handleError(error!!)
            }
        })
    }
}