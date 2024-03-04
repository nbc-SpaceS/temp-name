package com.example.seoulpublicservice.ui.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seoulpublicservice.R

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // 카테고리 프래그먼트를 추가합니다.
        supportFragmentManager.beginTransaction()
            .replace(R.id.category_fragment_container, CategoryFragment())
            .commit()
    }
}