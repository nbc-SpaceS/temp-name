package com.wannabeinseoul.seoulpublicservice.util

import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.seoul.Row

object RoomRowMapper {
    /**
     * @property mappingRoomToRow Room에서 사용하는 [ReservationEntity] 타입을 API에서 사용하는 [Row] 타입으로 변환
     * @param reservationEntity Room에서 사용하는 [ReservationEntity]타입
     * @return API값을 받아오는 [Row]타입
     */
    fun mappingRoomToRow(reservationEntity: ReservationEntity): Row {
        return Row(
            div = "",
            service = "",
            gubun = reservationEntity.GUBUN,
            svcid = reservationEntity.SVCID,
            maxclassnm = reservationEntity.MAXCLASSNM,
            minclassnm = reservationEntity.MINCLASSNM,
            svcstatnm = reservationEntity.SVCSTATNM,
            svcnm = reservationEntity.SVCNM,
            payatnm = reservationEntity.PAYATNM,
            placenm = reservationEntity.PLACENM,
            usetgtinfo = reservationEntity.USETGTINFO,
            svcurl = reservationEntity.SVCURL,
            x = reservationEntity.X,
            y = reservationEntity.Y,
            svcopnbgndt = reservationEntity.SVCOPNBGNDT,
            svcopnenddt = reservationEntity.SVCOPNENDDT,
            rcptbgndt = reservationEntity.RCPTBGNDT,
            rcptenddt = reservationEntity.RCPTENDDT,
            areanm = reservationEntity.AREANM,
            imgurl = reservationEntity.IMGURL,
            dtlcont = reservationEntity.DTLCONT,
            telno = reservationEntity.TELNO,
            vMax = reservationEntity.V_MAX,
            vMin = reservationEntity.V_MIN,
            revstdday = reservationEntity.REVSTDDAY.toLong(),
            revstddaynm = reservationEntity.REVSTDDAYNM
        )
    }

    /**
     * @property mappingRoomToRow [ReservationEntity] 타입의 List를 [Row] 타입의 List로 변환
     * @param reservationEntities [ReservationEntity]타입의 List
     * @return [Row]타입의 List
     */
    fun mappingRoomToRow(reservationEntities: Collection<ReservationEntity>): List<Row> =
        reservationEntities.map { mappingRoomToRow(it) }

    /**
     * @property mappingRowToRoom API값을 받는 [Row] 타입을 Room에서 사용하는 [ReservationEntity] 타입으로 변환
     * @param row API값을 받는 [Row] 타입
     * @return Room에서 사용하는 [ReservationEntity] 타입
     */
    fun mappingRowToRoom(row: Row): ReservationEntity {
        return ReservationEntity(
            AREANM = row.areanm,
            DTLCONT = row.dtlcont,
            GUBUN = row.gubun,
            IMGURL = row.imgurl,
            MAXCLASSNM = row.maxclassnm,
            MINCLASSNM = row.minclassnm,
            PAYATNM = row.payatnm,
            PLACENM = row.placenm,
            RCPTBGNDT = row.rcptbgndt,
            RCPTENDDT = row.rcptenddt,
            REVSTDDAY = row.revstdday.toString(),
            REVSTDDAYNM = row.revstddaynm,
            SVCID = row.svcid,
            SVCNM = row.svcnm,
            SVCOPNBGNDT = row.svcopnbgndt,
            SVCOPNENDDT = row.svcopnenddt,
            SVCSTATNM = row.svcstatnm,
            SVCURL = row.svcurl,
            TELNO = row.telno,
            USETGTINFO = row.usetgtinfo,
            V_MAX = row.vMax,
            V_MIN = row.vMin,
            X = row.x,
            Y = row.y
        )
    }

    /**
     * @property mappingRowToRoom [Row] 타입의 List를 [ReservationEntity] 타입의 List로 변환
     * @param rows [Row]타입의 List
     * @return [ReservationEntity]타입의 List
     */
    fun mappingRowToRoom(rows: Collection<Row>): List<ReservationEntity> =
        rows.map { mappingRowToRoom(it) }
}