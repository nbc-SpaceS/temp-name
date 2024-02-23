package com.example.seoulpublicservice.databases

import kotlinx.coroutines.flow.Flow

interface ReservationDataSource {
    fun insert(reservationEntity: ReservationEntity)
    fun delete(reservationEntity: ReservationEntity)
    fun getAll() : Flow<List<ReservationEntity>>
    fun getItemsWithType(type: String) : Flow<List<ReservationEntity>>
    fun getItemsWithTypeExtended(type: String) : Flow<List<ReservationEntity>>
}