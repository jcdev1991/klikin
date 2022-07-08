package com.example.klikin.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.klikin.R
import com.example.klikin.databinding.DetailActivityBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import java.lang.Exception

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: DetailActivityBinding
    private lateinit var map: GoogleMap
    private var name: String = ""
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        setData()
        setOnCLickListener()
        setMap()
    }

    private fun setData() {
        binding.appbar.ivBack.visibility = View.VISIBLE
        if (!intent.getStringExtra("urlPhoto").isNullOrEmpty()) {
            Picasso.get().load(intent.getStringExtra("urlPhoto")).into(binding.ivPhoto)
        }
        name = intent.getStringExtra("name").toString().replace("-", " ").capitalize()
        binding.appbar.tvTitle.text = name
        binding.tvName.text = name
        binding.tvCategory.text = intent.getStringExtra("category").toString()
        binding.tvShortDescription.text = intent.getStringExtra("shortDescription").toString()
        binding.tvLongDescription.text = intent.getStringExtra("longDescription").toString()
        latitude = intent.getStringExtra("latitude")!!.toDouble()
        longitude = intent.getStringExtra("longitude")!!.toDouble()
    }

    private fun setOnCLickListener() {
        binding.appbar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun setMap() {
        try {
            val mapFragment = supportFragmentManager.findFragmentById(R.id.frag_map) as SupportMapFragment
            mapFragment.getMapAsync { googleMap ->
                map = googleMap
                createMarker()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createMarker() {
        val coordinates = LatLng(latitude, longitude)
        val marker = MarkerOptions().position(coordinates).title(name)
        map.addMarker(marker)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 18f), 4000, null)
    }
}