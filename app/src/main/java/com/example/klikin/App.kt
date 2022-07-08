package com.example.klikin

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class App : Application() {

    companion object {
        private val TAG = App::class.java.simpleName

        lateinit var instance: App
            private set

        fun getContext(): Context {
            return instance.applicationContext
        }
    }

    var requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                field = Volley.newRequestQueue(applicationContext)
                return field
            }
            return field
        }
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        req.setShouldCache(false);
        req.retryPolicy = DefaultRetryPolicy(30 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue?.add(req)
        Log.d("Volley", tag)
    }
}