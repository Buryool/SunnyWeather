package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

//(TODO)这个类是干啥用的？
class SunnyWeatherApplication: Application() {
    companion object {
        const val TOKEN = "8ii2ax5MWpvL45Be"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}