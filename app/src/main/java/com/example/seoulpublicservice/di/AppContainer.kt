package com.example.seoulpublicservice.di

import com.example.seoulpublicservice.BuildConfig
import com.example.seoulpublicservice.seoul.SeoulApiService
import com.example.seoulpublicservice.seoul.SeoulPublicRepositoryImpl
import com.example.seoulpublicservice.usecase.GetAllFirst1000UseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

///** 클린아키텍쳐 하다가 정 안되면 그냥 이렇게 전역으로 놓고 쓰면 어디서든 사용 가능... */
//val myContainer: AppContainer = DefaultAppContainer()

/** Dependency Injection container */
interface AppContainer {
    val getAllFirst1000UseCase: GetAllFirst1000UseCase
}

class DefaultAppContainer : AppContainer {
    // TODO: retrofit 관련 로직들 따로 빼야 하나
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

    private val seoulPublicRepository by lazy { SeoulPublicRepositoryImpl(retrofitService) }

//    override val seoulPublicRepository: SeoulPublicRepository by lazy {
//        SeoulPublicRepositoryImpl(retrofitService)
//    }

    override val getAllFirst1000UseCase: GetAllFirst1000UseCase by lazy {
        GetAllFirst1000UseCase(seoulPublicRepository)
    }
}
