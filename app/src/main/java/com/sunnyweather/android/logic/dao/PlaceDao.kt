package com.sunnyweather.android.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place

object PlaceDao {
    // 获取到SharedPreferences对象
    private fun sharedPreferences() = SunnyWeatherApplication.context.getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)

    // 将Place对象存储到SharedPreferences文件中
    fun savePlace(place: Place){
        sharedPreferences().edit{
            // 利用Gson将对象转换成一个JSON字符串后存储
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        // 取出JSON字符串后用Gson转换成对象
        return Gson().fromJson(placeJson, Place::class.java)
    }

    // 判断是否有数据已被存储
    fun isPlaceSaved() = sharedPreferences().contains("place")
}