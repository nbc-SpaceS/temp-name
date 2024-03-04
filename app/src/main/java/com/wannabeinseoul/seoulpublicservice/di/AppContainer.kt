package com.wannabeinseoul.seoulpublicservice.di

import android.content.Context
import com.getkeepsafe.relinker.BuildConfig
import com.wannabeinseoul.seoulpublicservice.databases.ReservationDatabase
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepository
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.FilterPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.FilterPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.PrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.PrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.RowPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RowPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.SavedPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.SavedPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulApiService
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepository
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.usecase.GetAll2000UseCase
import com.wannabeinseoul.seoulpublicservice.usecase.GetDetailSeoulUseCase
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
    val seoulPublicRepository: SeoulPublicRepository
    val getAll2000UseCase: GetAll2000UseCase
    val getDetailSeoulUseCase: GetDetailSeoulUseCase
    val prefRepository: PrefRepository
    val rowPrefRepository: RowPrefRepository
    val regionPrefRepository: RegionPrefRepository
    val filterPrefRepository: FilterPrefRepository
    val idPrefRepository: IdPrefRepository
    val reservationRepository: ReservationRepository
    val dbMemoryRepository: DbMemoryRepository
    val savedPrefRepository: SavedPrefRepository
}

class DefaultAppContainer(context: Context, getAppRowList: () -> List<Row>) : AppContainer {
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

    override val seoulPublicRepository by lazy { SeoulPublicRepositoryImpl(retrofitService) }

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

    override val getDetailSeoulUseCase by lazy {
        GetDetailSeoulUseCase(
            seoulPublicRepository = seoulPublicRepository,
            prefRepository = prefRepository
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

    override val filterPrefRepository: FilterPrefRepository by lazy {
        FilterPrefRepositoryImpl(context = context)
    }

    override val idPrefRepository: IdPrefRepository by lazy {
        IdPrefRepositoryImpl(context = context)
    }

    /** Room과 관련된 Repository에 의존성 주입?? */
    private val database by lazy { ReservationDatabase.getDatabase(context) }
    override val reservationRepository by lazy {
        ReservationRepositoryImpl(database.getReservation())
    }

    override val dbMemoryRepository: DbMemoryRepository by lazy {
        DbMemoryRepositoryImpl(getAppRowList)
    }

    override val savedPrefRepository: SavedPrefRepository by lazy {
        SavedPrefRepositoryImpl(context)
    }

}
