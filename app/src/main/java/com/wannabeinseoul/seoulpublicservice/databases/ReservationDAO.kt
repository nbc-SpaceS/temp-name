package com.wannabeinseoul.seoulpublicservice.databases

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

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
     * @property getAll ReservationEntity 속성을 가지는 모든 값을 공백이 들어오는 부분 불러오기
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


    @Query("SELECT * FROM ReservationEntity " +
        "WHERE AREANM IS NOT NULL AND AREANM != '' " +
        "AND DTLCONT IS NOT NULL AND DTLCONT != '' " +
        "AND IMGURL IS NOT NULL AND IMGURL != '' " +
        "AND RCPTBGNDT IS NOT NULL AND RCPTBGNDT != '' " +
        "AND RCPTENDDT IS NOT NULL AND RCPTENDDT != '' " +
        "AND REVSTDDAYNM IS NOT NULL AND REVSTDDAYNM != '' " +
        "AND SVCOPNBGNDT IS NOT NULL AND SVCOPNBGNDT != '' " +
        "AND SVCOPNENDDT IS NOT NULL AND SVCOPNENDDT != '' " +
        "AND SVCID NOT LIKE 'XML%'")
    fun getNOTBlank() : List<ReservationEntity>


    @Query("SELECT DISTINCT MINCLASSNM FROM ReservationEntity")
    fun getSubList() : List<String>
    @Query("SELECT DISTINCT AREANM FROM ReservationEntity")
    fun getLocList() : List<String>
    @Query("SELECT DISTINCT SVCSTATNM FROM ReservationEntity")
    fun getSvcList() : List<String>
    @Query("SELECT DISTINCT PAYATNM FROM ReservationEntity")
    fun getPayList() : List<String>

    @RawQuery
    suspend fun putQueries(query: SupportSQLiteQuery): List<ReservationEntity>

    @Query("SELECT * FROM ReservationEntity WHERE SVCID IS (:svcID)")
    fun getService(svcID: String) : ReservationEntity

    @Query("SELECT * FROM ReservationEntity " +
            "WHERE AREANM IS NOT NULL AND AREANM != '' " +
            "AND DTLCONT IS NOT NULL AND DTLCONT != '' " +
            "AND IMGURL IS NOT NULL AND IMGURL != '' " +
            "AND RCPTBGNDT IS NOT NULL AND RCPTBGNDT != '' " +
            "AND RCPTENDDT IS NOT NULL AND RCPTENDDT != '' " +
            "AND REVSTDDAYNM IS NOT NULL AND REVSTDDAYNM != '' " +
            "AND SVCOPNBGNDT IS NOT NULL AND SVCOPNBGNDT != '' " +
            "AND SVCOPNENDDT IS NOT NULL AND SVCOPNENDDT != '' " +
            "AND (X IS NOT NULL OR X != '') AND (Y IS NOT NULL OR Y != '') " +
            "AND SVCID NOT LIKE 'XML%'")
    fun getNOTBlankInMaps() : List<ReservationEntity>
}