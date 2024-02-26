package com.example.seoulpublicservice.databases

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * API 받아올 데이터를 담는 테이블
 *
 * 기본키 : 서비스ID
 *
 * 필드 : 25개 (후순위로 50개 포함)
 */
@Dao
interface ReservationDAO {
    /**
     * @property insertAll ReservationEntity라는 테이블에 [ReservationEntity] 타입의 List를 모두 삽입
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reservationEntities: List<ReservationEntity>)

    /**
     * @property deleteAll ReservationEntity라는 테이블에서 모든 값을 지우기
     */
    @Query("DELETE FROM ReservationEntity")
    suspend fun deleteAll()


    /**
     * @property getAll ReservationEntity 속성을 가지는 모든 값을 불러오기
     */
    @Query("SELECT * FROM ReservationEntity")
    fun getAll() : List<ReservationEntity>

    /**
     * @property getItemsWithBigType 대분류명을 type에 받아 해당하는 아이템만 블러올 수 있는 함수
     * @param type 대분류명
     * (체육시설 = 체육, 시설대관 = 공간, 교육 = 교육, 문화행사 = 문화, 진료 = 진료)
     */
    @Query("SELECT * FROM ReservationEntity WHERE :type IN (MAXCLASSNM)")
    fun getItemsWithBigType(type: String) : List<ReservationEntity>

    /**
     * @property getItemsWithSmallType 소분류명을 type에 받아 해당하는 아이템만 블러올 수 있는 함수
     * @param type 소분류명
     */
    @Query("SELECT * FROM ReservationEntity WHERE :type IN (MINCLASSNM)")
    fun getItemsWithSmallType(type: String) : List<ReservationEntity>
}