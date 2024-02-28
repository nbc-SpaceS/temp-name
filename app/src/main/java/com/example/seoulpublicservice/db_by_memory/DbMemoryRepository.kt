package com.example.seoulpublicservice.db_by_memory

import com.example.seoulpublicservice.seoul.Row

interface DbMemoryRepository {
    fun getFiltered(
        minclassnm: List<String>? = null,
        areanm: List<String>? = null,
        svcstatnm: List<String>? = null,
        payatnm: List<String>? = null
    ): List<Row>

    fun getHaveLocation(): List<Row>
}

class DbMemoryRepositoryImpl(private val getAppRowList: () -> List<Row>) : DbMemoryRepository {
    override fun getFiltered(
        minclassnm: List<String>?,
        areanm: List<String>?,
        svcstatnm: List<String>?,
        payatnm: List<String>?
    ): List<Row> {
        return getHaveLocation().filter {
            (minclassnm.isNullOrEmpty() || it.minclassnm in minclassnm) &&
                    (areanm.isNullOrEmpty() || it.areanm in areanm) &&
                    (svcstatnm.isNullOrEmpty() || it.svcstatnm in svcstatnm) &&
                    (payatnm.isNullOrEmpty() || it.payatnm in payatnm)
        }
    }

    override fun getHaveLocation() =
        getAppRowList().filter { it.x.isBlank().not() && it.y.isBlank().not() }
}
