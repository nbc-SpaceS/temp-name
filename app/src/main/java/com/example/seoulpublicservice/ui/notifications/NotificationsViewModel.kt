package com.example.seoulpublicservice.ui.notifications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.seoulpublicservice.SeoulPublicServiceApplication
import com.example.seoulpublicservice.pref.PrefRepository
import com.example.seoulpublicservice.pref.RowPrefRepository
import com.example.seoulpublicservice.seoul.Row
import com.example.seoulpublicservice.usecase.GetAllFirst1000UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class NotificationsViewModel(
    private val getAllFirst1000UseCase: GetAllFirst1000UseCase,
    private val rowPrefRepository: RowPrefRepository,
    private val prefRepository: PrefRepository
) : ViewModel() {

    private val _text: MutableLiveData<String> =
        MutableLiveData("_text 라이브데이터 초기값 ㅁㄴㅇㄹㅁㄴㅇㄹ")
    val text: LiveData<String> = _text

    private var rowList: List<Row> = emptyList()
    private val random = Random

    private val tempKeyRowsSavedTime = "tempKeyRowsSavedTime"

    fun setRandomOne() {
        if (rowList.isEmpty()) viewModelScope.launch(Dispatchers.IO) {
            var isRecent = false
            val rowsSavedTime = prefRepository.load(tempKeyRowsSavedTime).toLongOrNull()
            if (rowsSavedTime == null) {
                Log.w(
                    "jj-노티뷰모델",
                    "prefRepository.load(tempKeyRowsSavedTime).toLongOrNull() == null"
                )
            } else {
                val timeDiff = System.currentTimeMillis() - rowsSavedTime
                Log.d("jj-노티뷰모델", "timeDiff: $timeDiff")
                isRecent = timeDiff < 180_000L
            }

            if (isRecent) rowList = rowPrefRepository.loadRows()
            if (rowList.isEmpty()) {
                rowList = getAllFirst1000UseCase()
                rowPrefRepository.saveRows(rowList)
                prefRepository.save(tempKeyRowsSavedTime, System.currentTimeMillis().toString())
            }

            val row = rowList.firstOrNull()
            if (row == null) {
                _text.postValue("rowList.size: ${rowList.size}\n\nrowList empty")
                Log.w("jj-노티뷰모델", "rowList is empty")
            } else {
                _text.postValue("rowList.size: ${rowList.size}\n\n$row")
                Log.d("jj-노티뷰모델", "id: ${row.svcid}")
            }
        }
        else {
            val row = rowList[random.nextInt(0, rowList.size)]
            _text.postValue("rowList.size: ${rowList.size}\n\n$row")
            Log.d("jj-노티뷰모델", "id: ${row.svcid}")
        }
    }

    companion object {
        /** 뷰모델팩토리에서 의존성주입을 해준다 */
        val factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SeoulPublicServiceApplication)
                val container = application.container
                NotificationsViewModel(
                    getAllFirst1000UseCase = container.getAllFirst1000UseCase,
                    rowPrefRepository = container.rowPrefRepository,
                    prefRepository = container.prefRepository
                )
            }
        }
    }

}
