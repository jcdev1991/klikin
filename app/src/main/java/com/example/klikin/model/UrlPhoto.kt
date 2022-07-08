package com.example.klikin.model

import com.example.klikin.util.base.BaseModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UrlPhoto : BaseModel() {

    @Expose
    @SerializedName("url")
    var url: String? = null

}