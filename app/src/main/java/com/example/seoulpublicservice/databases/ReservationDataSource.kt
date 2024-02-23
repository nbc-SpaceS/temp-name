package com.example.seoulpublicservice.databases

import kotlinx.coroutines.flow.Flow

interface ReservationDataSource {
    suspend fun insert(reservationEntity: ReservationEntity)
    suspend fun delete(reservationEntity: ReservationEntity)
    suspend fun getAll() : Flow<List<ReservationEntity>>
    suspend fun getItemsWithType(type: String) : Flow<List<ReservationEntity>>
    suspend fun getItemsWithTypeExtended(type: String) : Flow<List<ReservationEntity>>
}