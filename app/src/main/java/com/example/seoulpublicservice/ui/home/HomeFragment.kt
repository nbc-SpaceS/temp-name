package com.example.seoulpublicservice.ui.home

import android.content.Context
import android.content.Intent
import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.seoulpublicservice.InterestRegionSelectActivity
import com.example.seoulpublicservice.R
import com.example.seoulpublicservice.SeoulPublicServiceApplication
import com.example.seoulpublicservice.adapter.SpinnerAdapter
import com.example.seoulpublicservice.databinding.FragmentHomeBinding
import com.example.seoulpublicservice.pref.RegionPrefRepository
import com.example.seoulpublicservice.ui.notifications.NotificationsFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val regionPrefRepository: RegionPrefRepository by lazy {
        (requireActivity().application as SeoulPublicServiceApplication).container.regionPrefRepository
    }
    private var fragmentContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout


        // 지역 선택 화면으로 이동
        binding.tvSelectArea.setOnClickListener {
            val intent = Intent(context, InterestRegionSelectActivity::class.java)
            startActivity(intent)
        }

        // 공지사항 화면으로 이동
        binding.ivNotification.setOnClickListener {

        }

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 5

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> FacilityFragment()
                    1 -> EducationFragment()
                    2 -> CultureEventFragment()
                    3 -> FacilityRentFragment()
                    4 -> MedicalFragment()
                    else -> Fragment()
                }
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "체육시설"
                1 -> tab.text = "교육"
                2 -> tab.text = "문화행사"
                3 -> tab.text = "시설대관"
                4 -> tab.text = "진료"
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()

        val selectedRegions = regionPrefRepository.load().toMutableList()
        if (selectedRegions.isNotEmpty()) {

            // 스피너에 관심지역 설정 항목 추가
            selectedRegions.add("관심지역 설정")
//            fragmentContext?.let {
//                val adapter = SpinnerAdapter(it, R.layout.item_spinner, selectedRegions)
//                binding.spinnerSelectArea.adapter = adapter
//                binding.spinnerSelectArea.setBackgroundResource(R.drawable.spinner_background)
//            }
            fragmentContext?.let {
                val adapter = ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, selectedRegions)
                binding.spinnerSelectArea.adapter = adapter
                binding.spinnerSelectArea.setBackgroundResource(R.drawable.spinner_background)
            }


            // Show the Spinner and hide the TextView
            binding.spinnerSelectArea.visibility = View.VISIBLE
            binding.tvSelectArea.visibility = View.GONE

            binding.spinnerSelectArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position).toString()

                    // "관심지역 설정" 항목을 선택하면 관심지역 설정 페이지로 이동
                    if (selectedItem == "관심지역 설정") {
                        val intent = Intent(context, InterestRegionSelectActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // 아무 항목도 선택되지 않았을 때의 동작
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragmentContext = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}