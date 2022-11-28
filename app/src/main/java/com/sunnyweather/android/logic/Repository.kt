package com.sunnyweather.android.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * 仓库层的统一封装入口
 */
object Repository {

    /**
     * 用于进一步简化该类中的接口方法，不用每个方法中都try catch一次，只需要统一编写封装一次就可以了
     * @param context CoroutineContext
     * @param block SuspendFunction0<Result<T>>
     * @return LiveData<Result<T>>
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) = liveData<Result<T>>(context) {
        val result = try {
            block()
        } catch (e: Exception){
            Result.failure<T>(e)
        }

        // 这里专门搭配liveData()方法使用，类似于调用LiveData的setValue()方法来通知数据变化，只不过这里无法直接获取返回的LiveData对象，所以用这种办法替代。
        emit(result)
    }

    fun searchPlaces(city: String) = fire(Dispatchers.IO){// 在子线程中以高并发形式运行
        val placeResponse = SunnyWeatherNetwork.searchPlace(city)
        Log.d("ForTest", "服务器响应状态为：${placeResponse.status}")
        if (placeResponse.status == "ok") { // 如果服务器响应结果为ok
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("响应状态为：${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            // 让请求实时数据和日常数据的请求并行执行，所以用了async
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }

            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }

            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()

            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok"){
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException("“实时”响应为：${realtimeResponse.status}“每日”响应为：${dailyResponse.status}")
                )
            }
        }
    }
}