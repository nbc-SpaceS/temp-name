package com.example.seoulpublicservice.seoul

import com.example.seoulpublicservice.BuildConfig
import retrofit2.Response
import retrofit2.http.GET

interface SeoulApiService {
    @GET("/${BuildConfig.SEOUL_KEY}/json/tvYeyakCOllect/1/1000/")
    suspend fun getAllFirst1000(): Response<SeoulDto>
}
