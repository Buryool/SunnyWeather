package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

class RealtimeResponse(val status: String, val result: Result) {
    // 这里将所有的数据模型类都定义在了RealtimeResponse内部，这样可以防止出现和其他接口的数据模型类同名冲突的情况
    data class Result(val realtime: RealTime)
    data class RealTime(val skycon: String, val temperature: Float, @SerializedName("air_quality") val airQuality: AirQuality)
    data class AirQuality(val aqi: AQI)
    data class AQI(val chn: Float)
}