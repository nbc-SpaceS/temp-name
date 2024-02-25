package com.example.seoulpublicservice.usecase

import android.util.Log
import com.example.seoulpublicservice.pref.PrefRepository
import com.example.seoulpublicservice.pref.RowPrefRepository
import com.example.seoulpublicservice.seoul.Row
import com.example.seoulpublicservice.seoul.SeoulPublicRepository

class GetAllFirst1000UseCase(
    private val seoulPublicRepository: SeoulPublicRepository,
    private val prefRepository: PrefRepository,
    private val rowPrefRepository: RowPrefRepository
) {

    private var rowList: List<Row> = emptyList()
    private val tempKeyRowsSavedTime = "tempKeyRowsSavedTime"

    suspend operator fun invoke(): List<Row> {
        var isRecent = false
        val rowsSavedTime = prefRepository.load(tempKeyRowsSavedTime).toLongOrNull()
        if (rowsSavedTime == null) {
            Log.w(
                "jj-GetAllFirst1000UseCase",
                "prefRepository.load(tempKeyRowsSavedTime).toLongOrNull() == null"
            )
        } else {
            val timeDiff = System.currentTimeMillis() - rowsSavedTime
            Log.d("jj-GetAllFirst1000UseCase", "timeDiff: $timeDiff")
            isRecent = timeDiff < 180_000L
        }

        if (isRecent) {
            rowList = rowPrefRepository.loadRows()
            if (rowList.isEmpty()) getAllFirst1000()
        } else {
            getAllFirst1000()
        }

        return rowList
    }

    private suspend fun getAllFirst1000() {
        rowList = seoulPublicRepository.getAllFirst1000()
        rowPrefRepository.saveRows(rowList)
        prefRepository.save(tempKeyRowsSavedTime, System.currentTimeMillis().toString())
    }

}
