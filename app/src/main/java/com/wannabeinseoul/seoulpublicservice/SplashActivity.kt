package com.wannabeinseoul.seoulpublicservice

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.wannabeinseoul.seoulpublicservice.databases.firebase.UserEntity
import com.wannabeinseoul.seoulpublicservice.databinding.ActivitySplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class SplashActivity : AppCompatActivity() {

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var initialLoadingFinished = false
        var createFinished = false

        /** 액티비티에서 의존성 주입 */
        val app = (application as SeoulPublicServiceApplication)
        val container = app.container

        app.initialLoadingFinished.let { livedata ->
            livedata.observe(this) {
                Log.d("jj-스플래시", "옵저버:initialLoadingFinished - $it")
                if (it != true) return@observe
                if (createFinished) {
                    Log.d("jj-스플래시", "옵저버에서 이동 (스플래시 create가 먼저 끝남, 일반적)")
                    moveToNextActivity()
                    return@observe
                }
                initialLoadingFinished = it
            }
            initialLoadingFinished = livedata.value!!
        }

        val loadedId = container.idPrefRepository.load()
        if (loadedId.isBlank()) {
            val id = UUID.randomUUID().toString()
            val user = UserEntity(
                userName = "익명-${id.substring(0..5)}",
                userProfileImage = "",
                userColor = "#" + (1..6).map { id.replace("-", "").random() }.joinToString(""),
                reviewIdList = emptyList()
            )
            container.idPrefRepository.save(id)
            container.userRepository.addUser(id, user)
            app.user = user
        } else {
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    app.user = container.userRepository.getUser(loadedId)
                }
            } catch (e: Throwable) {
                Log.e("jj-스플래시", "userRepository.getUser 과정에서 에러. loadedId: $loadedId, e: $e")
            }
        }

        container.filterPrefRepository.clearData()

//        Handler(Looper.getMainLooper()).postDelayed(Runnable {
//            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
//            finish()
//        }, 500)

        if (initialLoadingFinished) {
            Log.d("jj-스플래시", "onCreate에서 이동 (메인 리스트 로딩이 먼저 끝남, 특이 케이스)")
            moveToNextActivity()
        } else createFinished = true
    }

    private fun moveToNextActivity() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }
}