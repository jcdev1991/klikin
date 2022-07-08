package com.example.klikin.repository

import com.android.volley.Response
import com.example.klikin.App
import com.example.klikin.util.AppConstants
import com.example.klikin.util.volley.VolleyArrayRequest
import com.example.klikin.util.volley.VolleyArrayCallback

object CommerceRepository : CommerceDataSource {

    private var instance: CommerceRepository? = null

    fun getInstance(): CommerceRepository? {
        if (instance == null) {
            instance = this
        }
        return instance
    }

    override fun getCommerces(callback: VolleyArrayCallback) {
        val jsonReq = object : VolleyArrayRequest(
            Method.GET, AppConstants.COMMERCES_URL, null,
            Response.Listener { callback.onResponse(it) },
            Response.ErrorListener { callback.onError(it) }) {
        }
        App.instance.addToRequestQueue(jsonReq, "getCommerces")
    }

}