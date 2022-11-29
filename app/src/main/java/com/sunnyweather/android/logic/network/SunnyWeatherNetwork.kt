package com.sunnyweather.android.logic.network

import android.util.Log
import com.sunnyweather.android.logic.model.DailyResponse
import com.sunnyweather.android.logic.model.PlaceResponse
import com.sunnyweather.android.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.ln

/**
 * 统一网络数据源访问入口，对所有网络请求的API进行封装
 */
object SunnyWeatherNetwork {
    private val placeService = ServiceCreator.create(PlaceService::class.java)
    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    // 当外部调用searchPlace()函数时，Retrofit就会立即发起网络请求，同时当前的协程也会被阻塞。直到服务器响应我们的请求之后，
    // await()函数会将解析出来的数据模型对象取出并返回，同时恢复当前的协程的执行。searchPlace()函数在得到await()函数的返回值
    // 后会将该数据再返回到上一层。
    suspend fun searchPlace(city: String): PlaceResponse{
        Log.d("ForTest", "Place的查询结果为：${placeService.searchPlaces(city).await()}")
        return placeService.searchPlaces(city).await()
    }

    suspend fun getDailyWeather(lng: String, lat: String): DailyResponse{
        return weatherService.getDailyWeather(lng, lat).await()
    }

    suspend fun getRealtimeWeather(lng: String, lat: String): RealtimeResponse{
        return weatherService.getRealtimeWeather(lng, lat).await()
    }

    /**
     * 相当于是整体定义了一遍回调的动作
     * @receiver Call<T>
     * @return T
     */
    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine {continuation ->
            enqueue(object : Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("响应体为空！"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })

        }
    }
}