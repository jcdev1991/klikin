package com.example.klikin.util.volley

import androidx.core.util.Pair
import com.android.volley.VolleyError
import org.json.JSONArray

interface VolleyArrayCallback {
    fun onResponse(response: Pair<JSONArray, MutableMap<String, String>>)
    fun onError(error: VolleyError?)
}