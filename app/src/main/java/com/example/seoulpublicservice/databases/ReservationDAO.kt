package com.example.seoulpublicservice.databases

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * API 받아올 데이터를 담는 테이블
 *
 * 기본키 : 서비스ID
 *
 * 필드 : 25개 (후순위로 50개 포함)
 */
interface ReservationDAO {
    @Insert
    fun insert(reservationEntity: ReservationEntity)

    @Delete
    fun delete(reservationEntity:ReservationEntity)


    /**
     * @property getAll ReservationEntity 속성을 가지는 모든 값을 불러오기
     */
    @Query("SELECT * FROM ReservationEntity")
    fun getAll() : List<ReservationEntity>

    /**
     * @property getItemsWithType 대분류명을 type에 받아 해당하는 아이템만 블러올 수 있는 함수
     * @param type 대분류명
     * (체육시설 = 체육, 시설대관 = 공간, 교육 = 교육, 문화행사 = 문화, 진료 = 진료)
     */
    @Query("SELECT * FROM ReservationEntity WHERE :type IN (MAXCLASSNM)")
    fun getItemsWithType(type: String) : List<ReservationEntity>

    /**
     * @property getItemsWithTypeExtended 소분류명을 type에 받아 해당하는 아이템만 블러올 수 있는 함수
     * @param type 소분류명
     */
    @Query("SELECT * FROM ReservationEntity WHERE :type IN (MINCLASSNM)")
    fun getItemsWithTypeExtended(type: String) : List<ReservationEntity>
}