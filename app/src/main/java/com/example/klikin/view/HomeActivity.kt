package com.example.klikin.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.klikin.R
import com.example.klikin.adapter.CategoryAdapter
import com.example.klikin.adapter.CommerceAdapter
import com.example.klikin.databinding.HomeActivityBinding
import com.example.klikin.model.Category
import com.example.klikin.model.Commerce
import com.example.klikin.util.AppConstants
import com.example.klikin.viewmodel.CommerceViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityBinding
    private lateinit var fusedLocation: FusedLocationProviderClient
    private var myLocation: Location? = Location("MyLocation")
    private var commerceAdapter: CommerceAdapter? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var commerces: ArrayList<Commerce> = ArrayList()
    private var categories: ArrayList<Category> = ArrayList()
    private var commerceViewModel: CommerceViewModel? = null
    private var isFiltered: Boolean = false
    private var minPage: Int = 0
    private var maxPage: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
        loading()
        binding.appbar.tvTitle.text = getString(R.string.appbar_title)
        commerceViewModel = ViewModelProvider(this).get(CommerceViewModel::class.java)
        setRecyclerCategory()
        setRecyclerCommerce()
        subscribeObserver()
        commerceViewModel?.getCommerces(minPage, maxPage)
        CoroutineScope(Dispatchers.Main)
    }

    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission()
                    return
                }
                fusedLocation.lastLocation.addOnSuccessListener {
                    myLocation = it
                }
            }
        } else {
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationMng = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationMng.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationMng.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                getLocation()
            } else {
                Toast.makeText(this, "DENEGADA LOCALIZACIÓN", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loading() {
        binding.llLoading.visibility = View.VISIBLE
    }

    private fun loaded() {
        binding.llLoading.visibility = View.GONE
    }

    private fun setRecyclerCategory() {
        createCategories()
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvCategories.also {
            it.layoutManager = layoutManager
            it.setHasFixedSize(true)
            it.itemAnimator = DefaultItemAnimator()
            categoryAdapter = CategoryAdapter(categories, object : CategoryAdapter.SetOnClickAdapterListener {
                override fun onClick(position: Int) {
                    isFiltered = true
                    when (position) {
                        0 -> filterCommerces(AppConstants.CATEGORY_SHOPPING)
                        1 -> filterCommerces(AppConstants.CATEGORY_FOOD)
                        2 -> filterCommerces(AppConstants.CATEGORY_BEAUTY)
                        3 -> filterCommerces(AppConstants.CATEGORY_LEISURE)
                        4 -> filterCommerces(AppConstants.CATEGORY_OTHER)
                        5 -> {
                            commerceAdapter!!.commerces = commerces
                            commerceAdapter!!.notifyDataSetChanged()
                            Toast.makeText(applicationContext, "FILTROS ANULADOS", Toast.LENGTH_SHORT).show()
                            isFiltered = false
                        }
                    }
                }
            })
            it.adapter = categoryAdapter
        }
    }

    private fun filterCommerces(category: String) {
        try {
            val filteredCommerces: ArrayList<Commerce> = ArrayList()
            if (category != "OTHER") {
                for (item in commerces) {
                    if (item.category == category) {
                        filteredCommerces.add(item)
                    }
                }
            } else {
                for (item in commerces) {
                    if (item.category != AppConstants.CATEGORY_SHOPPING && item.category != AppConstants.CATEGORY_FOOD
                        && item.category != AppConstants.CATEGORY_BEAUTY && item.category != AppConstants.CATEGORY_LEISURE) {
                        filteredCommerces.add(item)
                    }
                }
            }
            commerceAdapter!!.commerces = filteredCommerces
            commerceAdapter!!.notifyDataSetChanged()
            Toast.makeText(this, "FILTRADO POR CATEGORÍA $category", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createCategories() {
        categories.add(Category(ContextCompat.getDrawable(this, R.drawable.icon_payment_regulated_parking_colour)!!, AppConstants.CATEGORY_SHOPPING))
        categories.add(Category(ContextCompat.getDrawable(this, R.drawable.icon_catering_colour)!!, AppConstants.CATEGORY_FOOD))
        categories.add(Category(ContextCompat.getDrawable(this, R.drawable.icon_car_wash_colour)!!, AppConstants.CATEGORY_BEAUTY))
        categories.add(Category(ContextCompat.getDrawable(this, R.drawable.icon_leisure_colour)!!, AppConstants.CATEGORY_LEISURE))
        categories.add(Category(ContextCompat.getDrawable(this, R.drawable.icon_cart_colour)!!, AppConstants.CATEGORY_OTHER))
        categories.add(Category(ContextCompat.getDrawable(this, R.drawable.placeholder)!!, "DELETE FILTERS"))
    }

    private fun setRecyclerCommerce() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvCommerces.also {
            it.layoutManager = layoutManager
            it.setHasFixedSize(true)
            it.itemAnimator = DefaultItemAnimator()
            commerceAdapter = CommerceAdapter(commerces, object : CommerceAdapter.SetOnClickAdapterListener {
                override fun onClick(position: Int) {
                    val intent = Intent(applicationContext, DetailActivity::class.java)
                    intent.putExtra("urlPhoto", commerces[position].urlPhoto?.url)
                    intent.putExtra("name", commerces[position].name)
                    intent.putExtra("category", commerces[position].category)
                    intent.putExtra("shortDescription", commerces[position].shortDescription)
                    intent.putExtra("longDescription", commerces[position].longDescription)
                    intent.putExtra("latitude", commerces[position].latitude)
                    intent.putExtra("longitude", commerces[position].longitude)
                    startActivity(intent)
                }
            })
            it.adapter = commerceAdapter
            commerceAdapter!!.notifyDataSetChanged()
        }
        setCommerceScrollListener()
    }

    private fun setCommerceScrollListener() {
        binding.rvCommerces.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                try {
                    val diff: Int = recyclerView.getChildAt(recyclerView.childCount - 1).bottom - (recyclerView.height + recyclerView.scrollY)
                    if (diff == 0) {
                        if (isFiltered) {
                            Toast.makeText(applicationContext, "ANULE EL FILTRO PARA PODER AÑADIR MÁS ELEMENTOS", Toast.LENGTH_SHORT).show()
                        } else {
                            Thread.sleep(1500)
                            loading()
                            minPage += 10
                            maxPage += 10
                            commerceAdapter!!.commerces = commerces
                            commerceViewModel?.getCommerces(minPage, maxPage)
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun subscribeObserver() {
        commerceViewModel!!.checkCommercesiveData.observe(this, {
            loading()
            commerces.addAll(it)
            for (item in commerces) {
                val commerceLocation = Location("CommerceLocation")
                commerceLocation.latitude = item.latitude!!.toDouble()
                commerceLocation.longitude = item.longitude!!.toDouble()
                val commerceDistance = myLocation!!.distanceTo(commerceLocation)
                item.distance = commerceDistance
            }
            commerces.sortBy { commerce -> commerce.distance }
            commerceAdapter!!.notifyDataSetChanged()
            binding.tvCommerceCounter.text = commerces.size.toString()
            loaded()
        })
    }

}