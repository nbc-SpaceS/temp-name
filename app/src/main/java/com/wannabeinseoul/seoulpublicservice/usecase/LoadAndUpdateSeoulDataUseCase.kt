package com.wannabeinseoul.seoulpublicservice.usecase

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.db_by_memory.DbMemoryRepository
import com.wannabeinseoul.seoulpublicservice.pref.PrefRepository
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepository
import com.wannabeinseoul.seoulpublicservice.util.toReservationEntityList
import com.wannabeinseoul.seoulpublicservice.util.toastLong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

private const val JJTAG = "jj-LoadAndUpdateSeoulDataUseCase"

// TODO: pref 레포 하나 파야할듯
private const val KEY_SAVED_TIME = "KEY_SAVED_TIME"

class LoadAndUpdateSeoulDataUseCase(
    private val context: Context,
    private val seoulPublicRepository: SeoulPublicRepository,
    private val prefRepository: PrefRepository,
    private val dbMemoryRepository: DbMemoryRepository,
    private val reservationRepository: ReservationRepositoryImpl
) {
    /** 코루틴 비동기 실행 */
    operator fun invoke(callback: () -> Unit) = CoroutineScope(Dispatchers.IO).launch {
        val cm: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val cap = cm.getNetworkCapabilities(cm.activeNetwork)
        if (cap == null ||
            (cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)).not()
        ) {
            withContext(Dispatchers.Main) {
                toastLong(
                    context,
                    "네트워크 연결이 불가능하므로 저장된 데이터로 표시됩니다."
                )
            }
            // TODO: post로 바뀌어서 로그 바로 안찍힘. 어차피 옮기면서 손보긴 할거다
            getFromDB()
            Log.d(
                JJTAG, "cm.getNetworkCapabilities(cm.activeNetwork): $cap\n" +
                        "DB에서 꺼냄(네트워크 불가): ${
                            dbMemoryRepository.getAll().toString().take(255)
                        }"
            )
            return@launch
        }

        var isOld = true
        val rowsSavedTime = prefRepository.load(KEY_SAVED_TIME)?.toLongOrNull()
        if (rowsSavedTime == null) {
            Log.w(
                JJTAG,
                "prefRepository.load(tempKeyRowsSavedTime).toLongOrNull() == null"
            )
        } else {
            val timeDiff = System.currentTimeMillis() - rowsSavedTime
            Log.d(JJTAG, "timeDiff: $timeDiff")
            isOld = timeDiff > 180_000L
        }

        coroutineScope {
            if (isOld) {
                getAndUpdateAll2000()
            } else {
                getFromDB()
                if (dbMemoryRepository.getAll().isEmpty()) getAndUpdateAll2000()
            }
        }


        callback()
    }

    // TODO: -ing 룸에 데이터 안들어감
    private suspend fun getAndUpdateAll2000() {
        try {
            withTimeout(10_000L) {
                withContext(Dispatchers.Main) {
                    dbMemoryRepository.setAll(
                        seoulPublicRepository.getAllParallel().toReservationEntityList()
                    )
                }

//                dbMemoryRepository.postAll(
//                    seoulPublicRepository.getAllParallel()
//                )
                val reservationEntities =
                    dbMemoryRepository.getAll()
                reservationRepository.deleteAll()
                reservationRepository.insertAll(reservationEntities)
                prefRepository
                    .save(KEY_SAVED_TIME, System.currentTimeMillis().toString())
            }
        } catch (e: Throwable) {
            Log.e(JJTAG, "서울API 통신 에러 (아마도 Timeout)", e)
            withContext(Dispatchers.Main) {
                toastLong(
                    context,
                    "데이터를 받아오는 과정에서 문제가 발생했습니다. 저장된 데이터로 표시됩니다.",
                )
            }
            getFromDB()
        }
    }

    private suspend fun getFromDB() {
        val reservationEntities = reservationRepository.getAll()
        withContext(Dispatchers.Main) {
            dbMemoryRepository.setAll(reservationEntities)
        }

//        dbMemoryRepository.postAll(reservationEntities.toRowList())
    }

}
