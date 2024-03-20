package com.wannabeinseoul.seoulpublicservice.di

import android.content.Context
import com.wannabeinseoul.seoulpublicservice.BuildConfig
import com.wannabeinseoul.seoulpublicservice.databases.ReservationDatabase
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.databases.firestore.ComplaintRepository
import com.wannabeinseoul.seoulpublicservice.databases.firestore.ComplaintRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.databases.firestore.ReviewRepository
import com.wannabeinseoul.seoulpublicservice.databases.firestore.ReviewRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.databases.firestore.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.databases.firestore.ServiceRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.databases.firestore.UserBanRepository
import com.wannabeinseoul.seoulpublicservice.databases.firestore.UserBanRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.databases.firestore.UserProfileRepository
import com.wannabeinseoul.seoulpublicservice.databases.firestore.UserProfileRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.databases.firestore.UserRepository
import com.wannabeinseoul.seoulpublicservice.databases.firestore.UserRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepository
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.CategoryPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.CategoryPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.FilterPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.FilterPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.PrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.PrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.RecentPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RecentPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.RecommendPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RecommendPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.RowPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RowPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.SavedPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.SavedPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.SearchPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.SearchPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulApiService
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepository
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.usecase.*
import com.wannabeinseoul.seoulpublicservice.weather.shorttime.WeatherApiService
import com.wannabeinseoul.seoulpublicservice.weather.shorttime.WeatherShortRepository
import com.wannabeinseoul.seoulpublicservice.weather.shorttime.WeatherShortRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.usecase.CheckComplaintSelfUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.CheckCredentialsUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.ComplaintUserUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.DeleteReviewUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.FilterServiceDataOnMapUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.GetAll2000UseCase
import com.wannabeinseoul.seoulpublicservice.usecase.GetDetailSeoulUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.GetReviewListUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.GetSavedServiceUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.LoadSavedFilterOptionsUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.MappingDetailInfoWindowUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.ReviseReviewUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.SaveFilterOptionsUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.SaveServiceUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.SearchServiceDataOnMapUseCase
import com.wannabeinseoul.seoulpublicservice.usecase.UploadReviewUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/** Dependency Injection container */
interface AppContainer {
    val seoulPublicRepository: SeoulPublicRepository
    val getAll2000UseCase: GetAll2000UseCase
    val getDetailSeoulUseCase: GetDetailSeoulUseCase
    val loadSavedFilterOptionsUseCase: LoadSavedFilterOptionsUseCase
    val filterServiceDataOnMapUseCase: FilterServiceDataOnMapUseCase
    val saveServiceUseCase: SaveServiceUseCase
    val mappingDetailInfoWindowUseCase: MappingDetailInfoWindowUseCase
    val getSavedServiceUseCase: GetSavedServiceUseCase
    val complaintUserUseCase: ComplaintUserUseCase
    val saveFilterOptionsUseCase: SaveFilterOptionsUseCase
    val uploadReviewUseCase: UploadReviewUseCase
    val getReviewListUseCase: GetReviewListUseCase
    val reviseReviewUseCase: ReviseReviewUseCase
    val checkCredentialsUseCase: CheckCredentialsUseCase
    val checkComplaintSelfUseCase: CheckComplaintSelfUseCase
    val searchServiceDataOnMapUseCase: SearchServiceDataOnMapUseCase
    val deleteReviewUseCase: DeleteReviewUseCase
    val prefRepository: PrefRepository
    val rowPrefRepository: RowPrefRepository
    val regionPrefRepository: RegionPrefRepository
    val filterPrefRepository: FilterPrefRepository
    val idPrefRepository: IdPrefRepository
    val reservationRepository: ReservationRepository
    val dbMemoryRepository: DbMemoryRepository
    val savedPrefRepository: SavedPrefRepository
    val searchPrefRepository: SearchPrefRepository
    val categoryPrefRepository: CategoryPrefRepository
    val recommendPrefRepository: RecommendPrefRepository
    val userProfileRepository: UserProfileRepository
    val userRepository: UserRepository
    val serviceRepository: ServiceRepository
    val reviewRepository: ReviewRepository
    val complaintRepository: ComplaintRepository
    val userBanRepository: UserBanRepository
    val recentPrefRepository: RecentPrefRepository
    val weatherShortRepository: WeatherShortRepository
}

class DefaultAppContainer(context: Context, getAppRowList: () -> List<Row>) : AppContainer {
    private val seoulApiBaseUrl = "http://openapi.seoul.go.kr:8088"

    private val baseUrlWeather = "http://apis.data.go.kr/1360000"

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

    private fun createRetrofit(baseUrl: String): Retrofit = Retrofit.Builder().also {
        it.addConverterFactory(GsonConverterFactory.create())
        it.baseUrl(baseUrl)
        it.client(createOkHttpClient())
    }.build()

    private val seoulRetrofit = createRetrofit(seoulApiBaseUrl)
    private val retrofitSeoulApiService: SeoulApiService by lazy {
        seoulRetrofit.create(SeoulApiService::class.java)
//    // 하나의 Retrofit은 하나의 Url만 사용가능
//    // 날씨(단기예보, 중기예보) 요청
//    private val retrofitWeather: Retrofit = Retrofit.Builder().also {
//        it.addConverterFactory(GsonConverterFactory.create())
//        it.baseUrl(baseUrlWeather)
//        it.client(createOkHttpClient())
//    }.build()
//
//    /** Retrofit service object for creating api calls */
//    private val retrofitService: SeoulApiService by lazy {
//        retrofit.create(SeoulApiService::class.java)
//    }
    }

    override val seoulPublicRepository: SeoulPublicRepository by lazy {
        SeoulPublicRepositoryImpl(retrofitSeoulApiService)
    }
//    private val retrofitServiceWeather: WeatherApiService by lazy {
//        retrofitWeather.create(WeatherApiService::class.java)
//    }
//
//    override val seoulPublicRepository by lazy { SeoulPublicRepositoryImpl(retrofitService) }
//
//    override val weatherShortRepository by lazy { WeatherShortRepositoryImpl(retrofitServiceWeather) }
//
//    //    override val seoulPublicRepository: SeoulPublicRepository by lazy {
////        SeoulPublicRepositoryImpl(retrofitService)
////    }

    override val getAll2000UseCase by lazy {
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

    override val loadSavedFilterOptionsUseCase by lazy {
        LoadSavedFilterOptionsUseCase(
            filterPrefRepository = filterPrefRepository
        )
    }

    override val filterServiceDataOnMapUseCase by lazy {
        FilterServiceDataOnMapUseCase(
            reservationRepository = reservationRepository,
            dbMemoryRepository = dbMemoryRepository
        )
    }

    override val saveServiceUseCase by lazy {
        SaveServiceUseCase(
            savedPrefRepository = savedPrefRepository
        )
    }

    override val mappingDetailInfoWindowUseCase by lazy {
        MappingDetailInfoWindowUseCase(
            savedPrefRepository = savedPrefRepository
        )
    }

    override val getSavedServiceUseCase by lazy {
        GetSavedServiceUseCase(
            savedPrefRepository = savedPrefRepository
        )
    }

    override val complaintUserUseCase by lazy {
        ComplaintUserUseCase(
            reviewRepository = reviewRepository,
            complaintRepository = complaintRepository
        )
    }

    override val saveFilterOptionsUseCase by lazy {
        SaveFilterOptionsUseCase(
            filterPrefRepository = filterPrefRepository
        )
    }

    override val uploadReviewUseCase by lazy {
        UploadReviewUseCase(
            idPrefRepository = idPrefRepository,
            reviewRepository = reviewRepository,
            userRepository = userRepository,
            serviceRepository = serviceRepository
        )
    }

    override val getReviewListUseCase by lazy {
        GetReviewListUseCase(
            idPrefRepository = idPrefRepository,
            serviceRepository = serviceRepository,
            userBanRepository = userBanRepository
        )
    }

    override val reviseReviewUseCase by lazy {
        ReviseReviewUseCase(
            idPrefRepository = idPrefRepository,
            reviewRepository = reviewRepository
        )
    }

    override val checkCredentialsUseCase by lazy {
        CheckCredentialsUseCase(
            idPrefRepository = idPrefRepository,
            reviewRepository = reviewRepository
        )
    }

    override val checkComplaintSelfUseCase by lazy {
        CheckComplaintSelfUseCase(
            idPrefRepository = idPrefRepository,
            userRepository = userRepository
        )
    }

    override val searchServiceDataOnMapUseCase by lazy {
        SearchServiceDataOnMapUseCase(
            reservationRepository = reservationRepository,
            dbMemoryRepository = dbMemoryRepository
        )
    }

    override val deleteReviewUseCase by lazy {
        DeleteReviewUseCase(
            reviewRepository = reviewRepository
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

    override val searchPrefRepository: SearchPrefRepository by lazy {
        SearchPrefRepositoryImpl(context)
    }

    override val categoryPrefRepository: CategoryPrefRepository by lazy {
        CategoryPrefRepositoryImpl(context)
    }

    override val recommendPrefRepository: RecommendPrefRepository by lazy {
        RecommendPrefRepositoryImpl(context)
    }

    override val userProfileRepository by lazy {
        UserProfileRepositoryImpl(userRepository)
    }

    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl()
    }

    override val serviceRepository by lazy {
        ServiceRepositoryImpl()
    }

    override val reviewRepository by lazy {
        ReviewRepositoryImpl()
    }

    override val complaintRepository by lazy {
        ComplaintRepositoryImpl()
    }

    override val userBanRepository by lazy {
        UserBanRepositoryImpl()
    }

    override val recentPrefRepository: RecentPrefRepository by lazy {
        RecentPrefRepositoryImpl(context)
    }
}
