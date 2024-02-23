package com.example.seoulpublicservice.databases.usecases

import com.example.seoulpublicservice.databases.ReservationDataSource
import com.example.seoulpublicservice.databases.ReservationEntity

class GetItemsWithTypeUseCase(private val reservationDataSource: ReservationDataSource) {
    suspend fun invoke(type: String) = reservationDataSource.getItemsWithType(type)
}