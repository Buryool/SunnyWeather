package com.sunnyweather.android.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Location

class WeatherViewModel: ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    // 界面相关的数据，放到ViewModel中可以保证在手机屏幕发生旋转的时候不丢失
    var locationLng = ""
    var locationLat = ""
    var placeName = ""

    val WeatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat: String){
        locationLiveData.value = Location(lng, lat)
    }
}