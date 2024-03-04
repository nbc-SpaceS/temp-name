package com.wannabeinseoul.seoulpublicservice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wannabeinseoul.seoulpublicservice.databinding.ActivitySplashBinding
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
        app.initialLoadingFinished.let { livedata ->
            livedata.observe(this) {
                Log.d("jj-스플래시", "initialLoadingFinished 옵저버 $it")
                if (it != true) return@observe
                if (createFinished) {
                    Log.d("jj-스플래시", "옵저버에서 이동 (스플래시 create가 먼저 끝남, 일반적)")
                    moveToNextActivity()
                    return@observe
                }
                initialLoadingFinished = true
            }
            initialLoadingFinished = livedata.value!!
        }

        val container = app.container
        if (container.idPrefRepository.load() == "") {
            val id = UUID.randomUUID().toString()
            container.idPrefRepository.save(id)
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