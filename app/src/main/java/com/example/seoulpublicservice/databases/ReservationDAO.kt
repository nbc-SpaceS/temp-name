package com.example.seoulpublicservice.databases

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.util.query

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

    /**
     * @property getItemsWithSmallTypes 소분류명 List를 types에 받아 해당하는 아이템들을 불러오기
     * @param types 소분류명 List
     * @return `List<ReservationEntity>`
     */
    @Query("SELECT * FROM ReservationEntity WHERE MINCLASSNM in (:types)")
    fun getItemsWithSmallTypes(types: List<String>) : List<ReservationEntity>

    /**
     * @property getLocation 지역명을 리스트로 받아 출력
     * @param types 지역명 리스트
     * @return `List<ReservationEntity>`
     */
    @Query("SELECT * FROM ReservationEntity WHERE AREANM in (:types)")
    fun getLocation(types: List<String>) : List<ReservationEntity>

    /**
     * @property getServiceState 서비스 상태를 리스트로 받아 출력
     * @param types 서비스 상태 리스트
     * @return `List<ReservationEntity>`
     */
    @Query("SELECT * FROM ReservationEntity WHERE SVCSTATNM in (:types)")
    fun getServiceState(types: List<String>) : List<ReservationEntity>

    /**
     * @property getPay 요금을 리스트로 받아 출력
     * @param types 요금 리스트
     * @return `List<ReservationEntity>`
     */
    @Query("SELECT * FROM ReservationEntity WHERE PAYATNM in (:types)")
    fun getPay(types: List<String>) : List<ReservationEntity>

    @Query("SELECT * FROM ReservationEntity" +
            " WHERE ((:typeMin LIKE '%' AND MINCLASSNM IN (:typeMin)) OR (MINCLASSNM))" +
            " AND ((:typeArea LIKE '%' AND AREANM IN (:typeArea)) OR (AREANM))" +
            " AND ((:typeSvc LIKE '%' AND SVCSTATNM IN (:typeSvc)) OR (SVCSTATNM))" +
            " AND ((:typePay LIKE '%' AND PAYATNM IN (:typePay)) OR (PAYATNM))")
    fun getQueries(typeMin: List<String>, typeArea: List<String>, typeSvc: List<String>, typePay: List<String>) : List<ReservationEntity>
}