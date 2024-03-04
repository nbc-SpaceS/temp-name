package com.wannabeinseoul.seoulpublicservice.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wannabeinseoul.seoulpublicservice.InterestRegionSelectActivity
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.adapter.ItemAdapter
import com.wannabeinseoul.seoulpublicservice.data.Item
import com.wannabeinseoul.seoulpublicservice.data.ItemRepository
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentHomeBinding
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val regionPrefRepository: RegionPrefRepository by lazy {
        (requireActivity().application as SeoulPublicServiceApplication).container.regionPrefRepository
    }
    private var fragmentContext: Context? = null
    private val items: List<Item> by lazy {
        val categories = listOf("Facility", "Education", "CultureEvent", "FacilityRent", "Medical")
        categories.flatMap { ItemRepository.getItems(it) }
    }
    private val itemAdapter: ItemAdapter by lazy { ItemAdapter(items) }

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

        binding.ivSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            val tabIndex = items.indexOfFirst { it.name.contains(query, ignoreCase = true) }
            if (tabIndex != -1) {
                binding.viewPager.currentItem = tabIndex
            }
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.etSearch.text.toString()
                val tabIndex = items.indexOfFirst { it.name.contains(query, ignoreCase = true) }
                if (tabIndex != -1) {
                    binding.viewPager.currentItem = tabIndex
                }
                true
            } else {
                false
            }
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

        itemAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()

        val selectedRegions = regionPrefRepository.load().toMutableList()
        if (selectedRegions.isNotEmpty()) {
            // 스피너에 관심지역 설정 항목 추가
            selectedRegions.add("관심지역 재설정")
            fragmentContext?.let {
                val adapter = ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, selectedRegions)
                binding.spinnerSelectArea.adapter = adapter
                binding.spinnerSelectArea.setBackgroundResource(R.drawable.spinner_background)
            }

            // 스피너를 보여주고 텍스트뷰를 숨김
            binding.spinnerSelectArea.visibility = View.VISIBLE
            binding.tvSelectArea.visibility = View.INVISIBLE

            // 스피너의 onItemSelectedListener를 설정
            binding.spinnerSelectArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedItem = parent.getItemAtPosition(position).toString()

                    // "관심지역 재설정" 항목을 선택하면 관심지역 설정 페이지로 이동
                    if (selectedItem == "관심지역 재설정") {
                        val intent = Intent(context, InterestRegionSelectActivity::class.java)
                        startActivity(intent)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    return
                }
            }
        } else {
            // 스피너를 숨기고 텍스트뷰를 보여줌
            binding.spinnerSelectArea.visibility = View.INVISIBLE
            binding.tvSelectArea.visibility = View.VISIBLE
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