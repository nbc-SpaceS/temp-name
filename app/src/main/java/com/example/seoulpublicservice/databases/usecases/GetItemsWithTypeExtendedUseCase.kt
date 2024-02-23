package com.example.seoulpublicservice.databases.usecases

import com.example.seoulpublicservice.databases.ReservationDataSource
import com.example.seoulpublicservice.databases.ReservationEntity

class GetItemsWithTypeExtendedUseCase(private val reservationDataSource: ReservationDataSource) {
    suspend fun invoke(type: String) = reservationDataSource.getItemsWithTypeExtended(type)
}