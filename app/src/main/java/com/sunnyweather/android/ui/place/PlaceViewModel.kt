package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place

class PlaceViewModel: ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    // 用于缓存界面上要显示的城市的数据
    val placeList = ArrayList<Place>()
    val placeLiveData = Transformations.switchMap(searchLiveData) {city ->
        Repository.searchPlaces(city)
    }

    // 因为LiveData是外面传进来的，所以用“关联值”转换了一下
    fun searchPlaces(city: String){
        searchLiveData.value = city
    }
}