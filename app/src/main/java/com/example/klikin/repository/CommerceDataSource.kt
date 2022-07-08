package com.example.klikin.repository

import com.example.klikin.util.volley.VolleyArrayCallback

interface CommerceDataSource {
    fun getCommerces(callback: VolleyArrayCallback)
}