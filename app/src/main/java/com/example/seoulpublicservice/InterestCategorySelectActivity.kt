package com.example.seoulpublicservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seoulpublicservice.databinding.ActivityInterestCategorySelectAcitivtyBinding


class InterestCategorySelectActivity : AppCompatActivity() {

    private val binding: ActivityInterestCategorySelectAcitivtyBinding by lazy {
        ActivityInterestCategorySelectAcitivtyBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvInterestCategorySelectSkipBtn.setOnClickListener {
            startActivity(Intent(this@InterestCategorySelectActivity, MainActivity::class.java))
            finish()
        }

        binding.clInterestCategorySelectService1.setOnClickListener {
            val dialog = SelectCategoryService1Dialog()
            dialog.show(
                supportFragmentManager, "DialogSelectCategoryService1"
            )
        }

        binding.clInterestCategorySelectService2.setOnClickListener {
            val dialog = SelectCategoryService2Dialog()
            dialog.show(
                supportFragmentManager, "DialogSelectCategoryService2"
            )
        }

        binding.clInterestCategorySelectService3.setOnClickListener {
            val dialog = SelectCategoryService3Dialog()
            dialog.show(
                supportFragmentManager, "DialogSelectCategoryService3"
            )
        }
        binding.clInterestCategorySelectService4.setOnClickListener {
            val dialog = SelectCategoryService4Dialog()
            dialog.show(
                supportFragmentManager, "DialogSelectCategoryService4"
            )
        }

        binding.clInterestCategorySelectService5.setOnClickListener {
            val dialog = SelectCategoryService5Dialog()
            dialog.show(
                supportFragmentManager, "DialogSelectCategoryService5"
            )
        }

    }
}