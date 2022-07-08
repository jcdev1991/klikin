package com.example.klikin.model

import com.example.klikin.util.base.BaseModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Commerce : BaseModel() {

    @Expose
    @SerializedName("logo")
    var urlPhoto: UrlPhoto? = null

    @Expose
    @SerializedName("slug")
    var name: String? = null

    @Expose
    @SerializedName("category")
    var category: String? = null

    @Expose
    @SerializedName("shortDescription")
    var shortDescription: String? = null

    @Expose
    @SerializedName("description")
    var longDescription: String? = null

    @Expose
    @SerializedName("latitude")
    var latitude: String? = null

    @Expose
    @SerializedName("longitude")
    var longitude: String? = null

    var distance: Float = 0f

    override fun toString(): String {
        return "$name"
    }
}