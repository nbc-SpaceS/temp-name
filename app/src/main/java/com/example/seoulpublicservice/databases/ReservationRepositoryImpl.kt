package com.example.seoulpublicservice.databases

import kotlinx.coroutines.flow.Flow

class ReservationRepositoryImpl(private val reservationDataSource: ReservationDataSource): ReservationRepository {
    override val allItems: Flow<List<ReservationEntity>>
        get() = TODO("Not yet implemented")

    override suspend fun insert(reservation: ReservationEntity) {

    }
}