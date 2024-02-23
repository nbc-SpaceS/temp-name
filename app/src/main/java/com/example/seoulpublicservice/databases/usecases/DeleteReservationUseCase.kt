package com.example.seoulpublicservice.databases.usecases

import com.example.seoulpublicservice.databases.ReservationDataSource
import com.example.seoulpublicservice.databases.ReservationEntity

class DeleteReservationUseCase(private val reservationDataSource: ReservationDataSource) {
    suspend fun invoke(reservationEntity: ReservationEntity) = reservationDataSource.delete(reservationEntity)
}