package com.example.seoulpublicservice.di

import com.example.seoulpublicservice.BuildConfig
import com.example.seoulpublicservice.seoul.SeoulApiService
import com.example.seoulpublicservice.seoul.SeoulPublicRepository
import com.example.seoulpublicservice.seoul.SeoulPublicRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/** AppContainer instance used by the rest of classes to obtain dependencies */
val myContainer: AppContainer = DefaultAppContainer()

/** Dependency Injection container */
interface AppContainer {
    val seoulPublicRepository: SeoulPublicRepository
}


class DefaultAppContainer : AppContainer {
    private val baseUrl = "http://openapi.seoul.go.kr:8088"

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG)
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        else
            interceptor.level = HttpLoggingInterceptor.Level.NONE

        return OkHttpClient.Builder().also {
            it.connectTimeout(20, TimeUnit.SECONDS)
            it.readTimeout(20, TimeUnit.SECONDS)
            it.writeTimeout(20, TimeUnit.SECONDS)
            it.addNetworkInterceptor(interceptor)
        }.build()
    }

    private val retrofit: Retrofit = Retrofit.Builder().also {
        it.addConverterFactory(GsonConverterFactory.create())
        it.baseUrl(baseUrl)
        it.client(createOkHttpClient())
    }.build()

    /** Retrofit service object for creating api calls */
    private val retrofitService: SeoulApiService by lazy {
        retrofit.create(SeoulApiService::class.java)
    }

    /** DI implementation for Mars photos repository */
    override val seoulPublicRepository: SeoulPublicRepository by lazy {
        SeoulPublicRepositoryImpl(retrofitService)
    }
}
