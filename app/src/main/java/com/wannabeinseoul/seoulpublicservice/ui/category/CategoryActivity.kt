package com.wannabeinseoul.seoulpublicservice.ui.category

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import com.wannabeinseoul.seoulpublicservice.R

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // 카테고리 프래그먼트를 추가
        supportFragmentManager.beginTransaction()
            .replace(R.id.category_fragment_container, CategoryFragment())
            .commit()

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        findViewById<View>(R.id.iv_category_back)?.setOnClickListener {
            onBackPressed()
        }
    }
        override fun onSupportNavigateUp(): Boolean {
            onBackPressed() // 뒤로가기 버튼 클릭 시 onBackPressed() 호출
            return true
        }
    }