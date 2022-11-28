package com.sunnyweather.android.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

/**
 * 仓库层的统一封装入口
 */
object Repository {
    fun searchPlaces(city: String) = liveData(Dispatchers.IO){// 在子线程中以高并发形式运行
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlace(city)
            Log.d("ForTest", "服务器响应状态为：${placeResponse.status}")
            if (placeResponse.status == "ok") { // 如果服务器响应结果为ok
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("响应状态为：${placeResponse.status}"))
            }
        } catch (e: Exception){
            Result.failure<List<Place>>(e)
        }

        // 这里专门搭配liveData()方法使用，类似于调用LiveData的setValue()方法来通知数据变化，只不过这里无法直接获取返回的LiveData对象，所以用这种办法替代。
        emit(result)
    }
}