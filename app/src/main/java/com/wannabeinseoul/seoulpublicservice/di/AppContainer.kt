package com.wannabeinseoul.seoulpublicservice.di

import android.content.Context
import com.wannabeinseoul.seoulpublicservice.BuildConfig
import com.wannabeinseoul.seoulpublicservice.databases.ReservationDatabase
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.databases.firestore.*
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepository
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.*
import com.wannabeinseoul.seoulpublicservice.seoul.Row
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulApiService
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepository
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.usecase.*
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
