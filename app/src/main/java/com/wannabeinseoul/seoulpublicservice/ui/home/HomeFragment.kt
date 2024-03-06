package com.wannabeinseoul.seoulpublicservice.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.wannabeinseoul.seoulpublicservice.InterestRegionSelectActivity
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.adapter.HomeSearchAdapter
import com.wannabeinseoul.seoulpublicservice.adapter.ItemAdapter
import com.wannabeinseoul.seoulpublicservice.adapter.SearchHistoryAdapter
import com.wannabeinseoul.seoulpublicservice.data.Item
import com.wannabeinseoul.seoulpublicservice.data.ItemRepository
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentHomeBinding
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.SearchPrefRepository
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val regionPrefRepository: RegionPrefRepository by lazy { (requireActivity().application as SeoulPublicServiceApplication).container.regionPrefRepository }
    private val searchPrefRepository: SearchPrefRepository by lazy { (requireActivity().application as SeoulPublicServiceApplication).container.searchPrefRepository }
    private val reservationRepository: ReservationRepository by lazy { (requireActivity().application as SeoulPublicServiceApplication).container.reservationRepository }
    private var fragmentContext: Context? = null
    private lateinit var popupWindow: PopupWindow
    private lateinit var recyclerView: RecyclerView

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

    @SuppressLint("ClickableViewAccessibility")
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
            val searchText = binding.etSearch.text.toString()
            performSearch(searchText)
        }

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = v.text.toString()
                performSearch(searchText)
                true
            } else {
                false
            }
        }

        binding.ivSearch.setOnClickListener {
            // 검색어를 가져옴
            val searchText = binding.etSearch.text.toString()

            // 검색을 수행하고 결과를 가져옴
            val searchResults = performSearch(searchText)

//            // 검색 결과를 RecyclerView의 어댑터에 설정
//            val adapter = HomeSearchAdapter(searchResults)
//            binding.rvSearchHistory.adapter = adapter

            // tv_service_list, tab_layout, view_pager를 숨김
            binding.tvServiceList.visibility = View.GONE
            binding.tabLayout.visibility = View.GONE
            binding.viewPager.visibility = View.GONE

//            // 검색 결과를 표시하는 RecyclerView를 보이게 함 (수정 필요)
//            recyclerView.visibility = View.VISIBLE

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

        // PopupWindow 생성 및 설정
        recyclerView = RecyclerView(requireContext()).apply {
            layoutManager = LinearLayoutManager(requireContext())
        }


        // PopupWindow 초기화
        popupWindow = PopupWindow(
            recyclerView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // PopupWindow의 배경색을 흰색으로 설정
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        // 화면 밖을 터치하면 PopupWindow가 닫히도록 설정
        popupWindow.isOutsideTouchable = true

        binding.etSearch.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // EditText의 너비를 측정
                val width = binding.etSearch.width

                // PopupWindow width 설정
                popupWindow.width = width

                // 더 이상 필요하지 않으므로 리스너를 제거
                binding.etSearch.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        binding.etSearch.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val recentSearches = searchPrefRepository.load()
                val adapter = SearchHistoryAdapter(recentSearches).apply {
                    onItemClickListener = object : SearchHistoryAdapter.OnItemClickedListener {
                        override fun onItemClick(item: String) {
                            binding.etSearch.setText(item)
                            performSearch(item)
                            popupWindow.dismiss()
                        }
                    }
                }
                recyclerView.adapter = adapter
                popupWindow.showAsDropDown(v)
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()

    }

    fun settingRegions(): String {
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


    private fun performSearch(query: String) = lifecycleScope.launch{
        searchPrefRepository.save(query)
        Log.d("Search", "Saved search query: $query") // 로그 찍기

        // searchText 메소드를 호출하여 검색 결과를 가져옴
        val searchResults = reservationRepository.searchText(query)

        // 검색 결과를 SearchHistoryAdapter애 전달하여 RecyclerView에 표시
        val adapter = SearchHistoryAdapter(searchResults.map { it.SVCNM })
        /*binding.rvSearchHistory.adapter = adapter*/
        recyclerView.adapter = adapter

        // rv_search_history RecyclerView를 보이게 설정
        /*binding.rvSearchHistory.visibility = View.VISIBLE*/

        // PopupWindow 표시
        popupWindow.showAsDropDown(binding.etSearch)

    }
}