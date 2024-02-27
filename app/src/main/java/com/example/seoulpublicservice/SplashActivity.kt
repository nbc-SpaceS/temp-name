package com.example.seoulpublicservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.seoulpublicservice.databinding.ActivitySplashBinding
import com.example.seoulpublicservice.di.DefaultAppContainer
import java.util.UUID

class SplashActivity : AppCompatActivity() {

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /** 액티비티에서 의존성 주입 */
        val container = DefaultAppContainer(this)
        if (container.idPrefRepository.load() == "") {
            val id = UUID.randomUUID().toString()
            container.idPrefRepository.save(id)
        }

        container.filterPrefRepository.clearData()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 500)
    }
}