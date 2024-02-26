package com.example.seoulpublicservice.di

import android.content.Context
import com.example.seoulpublicservice.BuildConfig
import com.example.seoulpublicservice.databases.ReservationDatabase
import com.example.seoulpublicservice.databases.ReservationRepository
import com.example.seoulpublicservice.databases.ReservationRepositoryImpl
import com.example.seoulpublicservice.pref.PrefRepository
import com.example.seoulpublicservice.pref.PrefRepositoryImpl
import com.example.seoulpublicservice.pref.RegionPrefRepository
import com.example.seoulpublicservice.pref.RegionPrefRepositoryImpl
import com.example.seoulpublicservice.pref.RowPrefRepository
import com.example.seoulpublicservice.pref.RowPrefRepositoryImpl
import com.example.seoulpublicservice.seoul.SeoulApiService
import com.example.seoulpublicservice.seoul.SeoulPublicRepositoryImpl
import com.example.seoulpublicservice.usecase.GetAll2000UseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

///** 클린아키텍쳐 하다가 정 안되면 그냥 이렇게 전역으로 놓고 쓰면 어디서든 사용 가능...할 줄 알았는데
// * SharedPreferences나 Room 등 context를 사용하는 것들이 있어서
// * 애플리케이션 클래스에서 만들어줄 수 밖에 없다. */
//val myContainer: AppContainer = DefaultAppContainer()

/** Dependency Injection container */
interface AppContainer {
    val getAll2000UseCase: GetAll2000UseCase
    val prefRepository: PrefRepository
    val rowPrefRepository: RowPrefRepository
    val regionPrefRepository: RegionPrefRepository
    val reservationRepository: ReservationRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
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

    override val getAll2000UseCase: GetAll2000UseCase by lazy {
        GetAll2000UseCase(
            seoulPublicRepository = seoulPublicRepository,
            prefRepository = prefRepository,
            rowPrefRepository = rowPrefRepository
        )
    }

    override val prefRepository: PrefRepository by lazy {
        PrefRepositoryImpl(context = context)
    }

    override val rowPrefRepository: RowPrefRepository by lazy {
        RowPrefRepositoryImpl(context = context)
    }

    override val regionPrefRepository: RegionPrefRepository by lazy {
        RegionPrefRepositoryImpl(context = context)
    }
    /** Room과 관련된 Repository에 의존성 주입?? */
    private val database by lazy { ReservationDatabase.getDatabase(context) }
    override val reservationRepository by lazy {
        ReservationRepositoryImpl(database.getReservation())
    }

}
