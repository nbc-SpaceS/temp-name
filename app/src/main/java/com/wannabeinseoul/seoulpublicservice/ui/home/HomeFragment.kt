package com.wannabeinseoul.seoulpublicservice.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wannabeinseoul.seoulpublicservice.pref.SearchPrefRepository
import com.wannabeinseoul.seoulpublicservice.InterestRegionSelectActivity
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.adapter.ItemAdapter
import com.wannabeinseoul.seoulpublicservice.data.Item
import com.wannabeinseoul.seoulpublicservice.data.ItemRepository
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.google.android.material.tabs.TabLayoutMediator
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val regionPrefRepository: RegionPrefRepository by lazy { (requireActivity().application as SeoulPublicServiceApplication).container.regionPrefRepository }
    private val searchPrefRepository: SearchPrefRepository by lazy { (requireActivity().application as SeoulPublicServiceApplication).container.searchPrefRepository }
    private var fragmentContext: Context? = null

    private val items: List<Item> by lazy {
        val categories = listOf("Facility", "Education", "CultureEvent", "FacilityRent", "Medical")
        categories.flatMap { ItemRepository.getItems(it) }
    }

    private val itemAdapter: ItemAdapter by lazy { ItemAdapter(items, settingRegions()) }

    private var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            settingRegions()
        }
    }

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

        settingRegions()

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout


//        // 지역 선택 화면으로 이동
//        binding.tvSelectArea.setOnClickListener {
//            val intent = Intent(context, InterestRegionSelectActivity::class.java)
//            startActivity(intent)
//        }

        binding.clHomeSetRegion.setOnClickListener {
            if (binding.clHomeRegionList.isVisible) {
                binding.ivHomeMoreBtn.setImageResource(R.drawable.ic_more)
                binding.viewControlSpinner.isVisible = false
            } else {
                binding.ivHomeMoreBtn.setImageResource(R.drawable.ic_less)
                binding.viewControlSpinner.isVisible = true
            }
            binding.clHomeRegionList.isVisible = !binding.clHomeRegionList.isVisible
        }

        binding.tvHomeReSelectRegionBtn.setOnClickListener {
            binding.clHomeRegionList.isVisible = false
            binding.tvHomeSelectRegion1.setTextColor(requireContext().getColor(R.color.unable_button_text))
            binding.tvHomeSelectRegion2.setTextColor(requireContext().getColor(R.color.unable_button_text))
            binding.tvHomeSelectRegion3.setTextColor(requireContext().getColor(R.color.unable_button_text))
            binding.ivHomeMoreBtn.setImageResource(R.drawable.ic_more)
            binding.viewControlSpinner.isVisible = false
            val intent = Intent(context, InterestRegionSelectActivity::class.java)
            resultLauncher.launch(intent)
        }

        binding.tvHomeSelectRegion1.setOnClickListener {
            binding.tvHomeCurrentRegion.text = binding.tvHomeSelectRegion1.text
            binding.tvHomeSelectRegion1.setTextColor(requireContext().getColor(R.color.point_color))
            binding.tvHomeSelectRegion2.setTextColor(requireContext().getColor(R.color.unable_button_text))
            binding.tvHomeSelectRegion3.setTextColor(requireContext().getColor(R.color.unable_button_text))
            regionPrefRepository.save(listOf(binding.tvHomeSelectRegion1.text.toString()))
        }

        binding.tvHomeSelectRegion2.setOnClickListener {
            binding.tvHomeCurrentRegion.text = binding.tvHomeSelectRegion2.text
            binding.tvHomeSelectRegion2.setTextColor(requireContext().getColor(R.color.point_color))
            binding.tvHomeSelectRegion1.setTextColor(requireContext().getColor(R.color.unable_button_text))
            binding.tvHomeSelectRegion3.setTextColor(requireContext().getColor(R.color.unable_button_text))
            regionPrefRepository.save(listOf(binding.tvHomeSelectRegion2.text.toString()))
        }

        binding.tvHomeSelectRegion3.setOnClickListener {
            binding.tvHomeCurrentRegion.text = binding.tvHomeSelectRegion3.text
            binding.tvHomeSelectRegion3.setTextColor(requireContext().getColor(R.color.point_color))
            binding.tvHomeSelectRegion1.setTextColor(requireContext().getColor(R.color.unable_button_text))
            binding.tvHomeSelectRegion2.setTextColor(requireContext().getColor(R.color.unable_button_text))
            regionPrefRepository.save(listOf(binding.tvHomeSelectRegion3.text.toString()))
        }

        binding.viewControlSpinner.setOnClickListener {
            binding.clHomeRegionList.isVisible = false
            binding.ivHomeMoreBtn.setImageResource(R.drawable.ic_more)
            binding.viewControlSpinner.isVisible = false
        }

        // 공지사항 화면으로 이동
        binding.ivNotification.setOnClickListener {

        }

        binding.ivSearch.setOnClickListener {

        }

        binding.etSearch.setOnClickListener {

        }

        binding.etSearch.setOnFocusChangeListener { _, _ ->

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

//        val selectedRegions = regionPrefRepository.load().toMutableList()
//        if (selectedRegions.isNotEmpty()) {
//            // 스피너에 관심지역 설정 항목 추가
//            selectedRegions.add("관심지역 재설정")
//            fragmentContext?.let {
//                val adapter = ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, selectedRegions)
//                binding.spinnerSelectArea.adapter = adapter
//                binding.spinnerSelectArea.setBackgroundResource(R.drawable.spinner_background)
//            }
//
//            // 스피너를 보여주고 텍스트뷰를 숨김
//            binding.spinnerSelectArea.visibility = View.VISIBLE
//            binding.tvSelectArea.visibility = View.INVISIBLE
//
//            // 스피너의 onItemSelectedListener를 설정
//            binding.spinnerSelectArea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                    val selectedItem = parent.getItemAtPosition(position).toString()
//
//                    // "관심지역 재설정" 항목을 선택하면 관심지역 설정 페이지로 이동
//                    if (selectedItem == "관심지역 재설정") {
//                        val intent = Intent(context, InterestRegionSelectActivity::class.java)
//                        startActivity(intent)
//                    }
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>) {
//                    return
//                }
//            }
//        } else {
//            // 스피너를 숨기고 텍스트뷰를 보여줌
//            binding.spinnerSelectArea.visibility = View.INVISIBLE
//            binding.tvSelectArea.visibility = View.VISIBLE
//        }
    }

    private fun settingRegions(): String {
        val selectedRegions = regionPrefRepository.load().toMutableList()

        return if (selectedRegions.isNotEmpty()) {
            binding.tvHomeCurrentRegion.text = selectedRegions[0]
            binding.tvHomeSelectRegion1.setTextColor(requireContext().getColor(R.color.point_color))
            when (selectedRegions.size) {
                1 -> {
                    binding.tvHomeSelectRegion1.text = selectedRegions[0]
                    binding.tvHomeSelectRegion1.isVisible = true
                    binding.tvHomeSelectRegion2.isVisible = false
                    binding.tvHomeSelectRegion3.isVisible = false
                }

                2 -> {
                    binding.tvHomeSelectRegion1.text = selectedRegions[0]
                    binding.tvHomeSelectRegion2.text = selectedRegions[1]
                    binding.tvHomeSelectRegion1.isVisible = true
                    binding.tvHomeSelectRegion2.isVisible = true
                    binding.tvHomeSelectRegion3.isVisible = false
                }

                3 -> {
                    binding.tvHomeSelectRegion1.text = selectedRegions[0]
                    binding.tvHomeSelectRegion2.text = selectedRegions[1]
                    binding.tvHomeSelectRegion3.text = selectedRegions[2]
                    binding.tvHomeSelectRegion1.isVisible = true
                    binding.tvHomeSelectRegion2.isVisible = true
                    binding.tvHomeSelectRegion3.isVisible = true
                }
            }
            selectedRegions[0]
        } else {
            binding.tvHomeCurrentRegion.text = "지역선택"
            binding.tvHomeSelectRegion1.isVisible = false
            binding.tvHomeSelectRegion2.isVisible = false
            binding.tvHomeSelectRegion3.isVisible = false
            "지역선택"
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