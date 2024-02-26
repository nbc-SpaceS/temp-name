package com.example.seoulpublicservice

import android.app.Application
import com.example.seoulpublicservice.di.AppContainer
import com.example.seoulpublicservice.di.DefaultAppContainer

class SeoulPublicServiceApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    private lateinit var _container: AppContainer
    val container get() = _container
    override fun onCreate() {
        super.onCreate()
        _container = DefaultAppContainer(this)
    }
}
