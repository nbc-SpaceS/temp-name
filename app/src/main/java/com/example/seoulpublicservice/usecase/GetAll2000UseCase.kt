package com.example.seoulpublicservice.usecase

import android.util.Log
import com.example.seoulpublicservice.pref.PrefRepository
import com.example.seoulpublicservice.pref.RowPrefRepository
import com.example.seoulpublicservice.seoul.Row
import com.example.seoulpublicservice.seoul.SeoulPublicRepository

class GetAll2000UseCase(
    private val seoulPublicRepository: SeoulPublicRepository,
    private val prefRepository: PrefRepository,
    private val rowPrefRepository: RowPrefRepository
) {

    private var rowList: List<Row> = emptyList()
    private val tempKeyRowsSavedTime = "tempKeyRowsSavedTime"

//    suspend operator fun invoke(): List<Row> {
//        var isRecent = false
//        val rowsSavedTime = prefRepository.load(tempKeyRowsSavedTime).toLongOrNull()
//        if (rowsSavedTime == null) {
//            Log.w(
//                "jj-GetAll2000UseCase",
//                "prefRepository.load(tempKeyRowsSavedTime).toLongOrNull() == null"
//            )
//        } else {
//            val timeDiff = System.currentTimeMillis() - rowsSavedTime
//            Log.d("jj-GetAll2000UseCase", "timeDiff: $timeDiff")
//            isRecent = timeDiff < 180_000L
//        }
//
//        if (isRecent) {
//            rowList = rowPrefRepository.loadRows()
//            if (rowList.isEmpty()) getAll2000()
//        } else {
//            getAll2000()
//        }
//
//        return rowList
//    }

    suspend operator fun invoke(): List<Row> = emptyList()

    private suspend fun getAll2000() {
        rowList = seoulPublicRepository.getAll2000()
        rowPrefRepository.saveRows(rowList)
        prefRepository.save(tempKeyRowsSavedTime, System.currentTimeMillis().toString())
    }

}
