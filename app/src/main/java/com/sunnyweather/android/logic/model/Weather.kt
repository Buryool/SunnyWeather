package com.sunnyweather.android.logic.model

/**
 * 该类用于将Realtime和Daily对象封装起来
 */
class Weather(val realTime: RealtimeResponse.RealTime, val daily: DailyResponse.Daily)