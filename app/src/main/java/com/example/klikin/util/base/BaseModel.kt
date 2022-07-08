package com.example.klikin.util.base

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.io.Serializable

open class BaseModel : Serializable {

    @Transient
    protected val gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    fun toJson(): String {
        return gson.toJson(this)
    }
}