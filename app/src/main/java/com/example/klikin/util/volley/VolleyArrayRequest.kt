package com.example.klikin.util.volley

import android.text.TextUtils
import androidx.core.util.Pair
import com.android.volley.*
import com.android.volley.Response.ErrorListener
import com.android.volley.Response.Listener
import com.android.volley.toolbox.HttpHeaderParser
import org.json.JSONArray
import org.json.JSONException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import kotlin.system.exitProcess

open class VolleyArrayRequest : Request<Pair<JSONArray, MutableMap<String, String>>> {

    private var listener: Listener<Pair<JSONArray, MutableMap<String, String>>>? = null
    private var params: MutableMap<String, String>? = null

    constructor(url: String?, params: MutableMap<String, String>?, reponseListener: Listener<Pair<JSONArray, MutableMap<String, String>>>, errorListener: ErrorListener)
            : super(Method.GET, url, errorListener) {
        this.listener = reponseListener
        this.params = params
    }

    constructor(method: Int, url: String?, params: MutableMap<String, String>?, reponseListener: Listener<Pair<JSONArray, MutableMap<String, String>>>, errorListener: ErrorListener)
            : super(method, url, errorListener) {
        this.listener = reponseListener
        this.params = params
    }

    override fun getParams(): MutableMap<String, String>? {
        return params
    }

    override fun getBodyContentType(): String {
        return "application/json"
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<Pair<JSONArray, MutableMap<String, String>>> {
        try {
            var cacheEntry: Cache.Entry? = HttpHeaderParser.parseCacheHeaders(response)
            if (cacheEntry == null) {
                cacheEntry = Cache.Entry()
            }
            val cacheHitButRefreshed = (3 * 1000).toLong()
            val cacheExpired = (10 * 60 * 1000).toLong()
            val now = System.currentTimeMillis()
            val softExpire = now + cacheHitButRefreshed
            val ttl = now + cacheExpired
            cacheEntry.data = response.data
            cacheEntry.softTtl = softExpire
            cacheEntry.ttl = ttl
            var headerValue: String? = response.headers?.get("Date")
            if (headerValue != null) {
                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue)
            }
            headerValue = response.headers?.get("Last-Modified")
            if (headerValue != null) {
                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue)
            }
            cacheEntry.responseHeaders = response.headers
            val jsonString = String(response.data, Charset.forName(HttpHeaderParser.parseCharset(response.headers)))
            if (TextUtils.isEmpty(jsonString)) {
                return Response.success(Pair(JSONArray("{}"), response.headers), HttpHeaderParser.parseCacheHeaders(response))
            }
            val pair = Pair(JSONArray(jsonString), response.headers)
            return Response.success(pair, cacheEntry)
        } catch (e: UnsupportedEncodingException) {
            return Response.error(ParseError(e))
        } catch (e: JSONException) {
            return Response.error(ParseError(e))
        }
    }

    override fun deliverError(error: VolleyError) {
        super.deliverError(error)
        try {
            if (error.networkResponse != null && error.networkResponse.statusCode == 401 || error.networkResponse.statusCode == 403) {
                exitProcess(0)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun deliverResponse(response: Pair<JSONArray, MutableMap<String, String>>) {
        listener!!.onResponse(response)
    }
}