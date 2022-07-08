package com.example.klikin.util.base

import androidx.lifecycle.ViewModel
import com.example.klikin.App
import com.google.gson.Gson
import com.google.gson.GsonBuilder

open class BaseViewModel : ViewModel() {

    @Transient
    protected val app: App = App.instance

    @Transient
    protected val gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    fun toJson(): String {
        return gson.toJson(this)
    }

}