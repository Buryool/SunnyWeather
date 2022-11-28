package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

/**
 * 该类按照返回数据的JSON格式定义
 * @property status String
 * @property places List<Place>
 * @constructor
 */
data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(val name: String, val location: Location, @SerializedName("formatted_address")val address: String)

data class Location(val lng: String, val lat: String)