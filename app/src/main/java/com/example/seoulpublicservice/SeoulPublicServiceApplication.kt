package com.example.seoulpublicservice

import android.app.Application
import android.util.Log
import com.example.seoulpublicservice.di.AppContainer
import com.example.seoulpublicservice.di.DefaultAppContainer
import com.example.seoulpublicservice.seoul.Row
import com.example.seoulpublicservice.util.RoomRowMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SeoulPublicServiceApplication : Application() {

    /** AppContainer instance used by the rest of classes to obtain dependencies */
    private lateinit var _container: AppContainer
    val container get() = _container

    private val tempKeyRowsSavedTime = "tempKeyRowsSavedTime"  // 나중에 이름 바꿀거. key 전역으로 관리할수도.

    private var _rowList: List<Row> = emptyList()
    val rowList: List<Row> get() = _rowList

    override fun onCreate() {
        super.onCreate()
        _container = DefaultAppContainer(this)

        CoroutineScope(Dispatchers.Default).launch { updateRowList() }
    }

    private suspend fun updateRowList() {
        // TODO: 네트워크 상태 확인해서 불가능하면 룸에서 꺼내오기

        var isOld = true
        val rowsSavedTime = container.prefRepository.load(tempKeyRowsSavedTime).toLongOrNull()
        if (rowsSavedTime == null) {
            Log.w(
                "jj-앱클래스",
                "prefRepository.load(tempKeyRowsSavedTime).toLongOrNull() == null"
            )
        } else {
            val timeDiff = System.currentTimeMillis() - rowsSavedTime
            Log.d("jj-앱클래스", "timeDiff: $timeDiff")
            isOld = timeDiff > 180_000L
        }

        coroutineScope {
            if (isOld) {
                getAndUpdateAll2000()
            } else {
                val reservationEntities = container.reservationRepository.getAllReservations()
                _rowList = RoomRowMapper.mappingRoomToRow(reservationEntities)
                if (_rowList.isEmpty()) getAndUpdateAll2000()
            }
        }
    }

    private suspend fun getAndUpdateAll2000() {
        _rowList = container.seoulPublicRepository.getAll2000()
        val reservationEntities = RoomRowMapper.mappingRowToRoom(_rowList)
        container.reservationRepository.deleteAll()
        container.reservationRepository.insertAll(reservationEntities)
        container.prefRepository.save(tempKeyRowsSavedTime, System.currentTimeMillis().toString())
    }

}
