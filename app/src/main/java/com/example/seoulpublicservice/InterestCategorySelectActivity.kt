package com.example.seoulpublicservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import com.example.seoulpublicservice.databinding.ActivityInterestRegionSelectAcitivtyBinding


class InterestCategorySelectActivity : AppCompatActivity() {

    private val binding: ActivityInterestRegionSelectAcitivtyBinding by lazy {
        ActivityInterestRegionSelectAcitivtyBinding.inflate(layoutInflater)
    }

    private val matchingRegion = hashMapOf(
        R.id.cb_interest_region_select_btn1 to 1,
        R.id.cb_interest_region_select_btn2 to 2,
        R.id.cb_interest_region_select_btn3 to 3,
        R.id.cb_interest_region_select_btn4 to 4,
        R.id.cb_interest_region_select_btn5 to 5,
        R.id.cb_interest_region_select_btn6 to 6,
        R.id.cb_interest_region_select_btn7 to 7,
        R.id.cb_interest_region_select_btn8 to 8,
        R.id.cb_interest_region_select_btn9 to 9,
        R.id.cb_interest_region_select_btn10 to 10,
        R.id.cb_interest_region_select_btn11 to 11,
        R.id.cb_interest_region_select_btn12 to 12,
        R.id.cb_interest_region_select_btn13 to 13,
        R.id.cb_interest_region_select_btn14 to 14,
        R.id.cb_interest_region_select_btn15 to 15,
        R.id.cb_interest_region_select_btn16 to 16,
        R.id.cb_interest_region_select_btn17 to 17,
        R.id.cb_interest_region_select_btn18 to 18,
        R.id.cb_interest_region_select_btn19 to 19,
        R.id.cb_interest_region_select_btn20 to 20,
        R.id.cb_interest_region_select_btn21 to 21,
        R.id.cb_interest_region_select_btn22 to 22,
        R.id.cb_interest_region_select_btn23 to 23,
        R.id.cb_interest_region_select_btn24 to 24,
        R.id.cb_interest_region_select_btn25 to 25
    )

    private val selectedRegionList = arrayListOf<Int>()

    private val listener = CompoundButton.OnCheckedChangeListener { view, isChecked ->
        val position = matchingRegion[view.id] ?: 0

        if (isChecked) {
            if (selectedRegionList.size == 3) {
                view.isChecked = false
                Toast.makeText(this@InterestCategorySelectActivity, "최대선택개수초과", Toast.LENGTH_SHORT).show()
            } else {
                selectCheckbox(position)
            }
        } else {
            unselectCheckbox(position)
        }
    }

    private fun selectCheckbox(num: Int) {
        Toast.makeText(this@InterestCategorySelectActivity, "${num} 선택", Toast.LENGTH_SHORT).show()
        selectedRegionList.add(matchingRegion.keys.first { num == matchingRegion[it] })
        enableOkayButton()
    }

    private fun unselectCheckbox(num: Int) {
        Toast.makeText(this@InterestCategorySelectActivity, "${num} 해제", Toast.LENGTH_SHORT).show()
        selectedRegionList.remove(matchingRegion.keys.first { num == matchingRegion[it] })
        enableOkayButton()
    }

    private fun enableOkayButton() {
        binding.btnInterestRegionSelectOkay.isEnabled = selectedRegionList.isNotEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) = with(binding) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ivInterestRegionSelectBackBtn.setOnClickListener {
            finish()
        }

        cbInterestRegionSelectBtn1.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn2.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn3.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn4.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn5.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn6.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn7.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn8.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn9.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn10.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn11.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn12.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn13.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn14.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn15.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn16.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn17.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn18.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn19.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn20.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn21.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn22.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn23.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn24.setOnCheckedChangeListener(listener)
        cbInterestRegionSelectBtn25.setOnCheckedChangeListener(listener)
    }
}