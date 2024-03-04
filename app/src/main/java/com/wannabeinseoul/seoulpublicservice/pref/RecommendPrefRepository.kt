package com.wannabeinseoul.seoulpublicservice.pref

import android.content.Context
import android.content.SharedPreferences
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.seoul.Row

class RecommendPrefRepository(context: Context) : PrefRepository {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("recommend_shared_prefs", Context.MODE_PRIVATE)

    override fun save(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun load(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }
    fun convertToRow(reservations: List<ReservationEntity>): List<Row> {
        return reservations.map { reservation ->
            Row(
                gubun = reservation.GUBUN,
                placenm = reservation.PLACENM,
                payatnm = reservation.PAYATNM,
                svcstatnm = reservation.SVCSTATNM,
                areanm = reservation.AREANM,
                imgurl = reservation.IMGURL,
                maxclassnm = "",
                minclassnm = "",
                rcptbgndt = "",
                rcptenddt = "",
                revstdday = "".toLong(),
                revstddaynm = "",
                svcid = "",
                svcnm = "",
                svcopnbgndt = "",
                svcopnenddt = "",
                svcurl = "",
                telno = "",
                usetgtinfo = "",
                vMax = "",
                vMin = "",
                x = "",
                y = "",
                dtlcont = "",
                div = "",
                service = ""
            )
        }
    }
}