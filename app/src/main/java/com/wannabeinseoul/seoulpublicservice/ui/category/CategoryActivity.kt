package com.wannabeinseoul.seoulpublicservice.ui.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.wannabeinseoul.seoulpublicservice.R

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // 카테고리 프래그먼트를 추가
        supportFragmentManager.beginTransaction()
            .replace(R.id.category_fragment_container, CategoryFragment().apply {
                arguments = bundleOf(
                    "category" to intent.getStringExtra("category"),
                    "region" to intent.getStringExtra("region")
                )
            })
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // 뒤로가기 버튼 클릭 시 onBackPressed() 호출
        return true
    }
}