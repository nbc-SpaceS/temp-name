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
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import androidx.activity.OnBackPressedCallback
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
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentHomeBinding
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.SearchPrefRepository
import com.wannabeinseoul.seoulpublicservice.ui.interestregionselect.InterestRegionSelectActivity
import com.wannabeinseoul.seoulpublicservice.ui.main.adapter.HomeSearchAdapter
import com.wannabeinseoul.seoulpublicservice.ui.main.adapter.SearchHistoryAdapter
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
        setupViewPager()
        setupBackPress()
        settingPopupWindow()

        with(binding) {
            clHomeSetRegion.setOnClickListener {
                if (clHomeRegionList.isVisible) {
                    ivHomeMoreBtn.setImageResource(R.drawable.ic_more)
                    viewControlSpinner.isVisible = false
                } else {
                    ivHomeMoreBtn.setImageResource(R.drawable.ic_less)
                    viewControlSpinner.isVisible = true
                }
                clHomeRegionList.isVisible = !clHomeRegionList.isVisible
            }

            tvHomeReSelectRegionBtn.setOnClickListener {
                clHomeRegionList.isVisible = false
                tvHomeSelectRegion1.setTextColor(requireContext().getColor(R.color.unable_button_text))
                tvHomeSelectRegion2.setTextColor(requireContext().getColor(R.color.unable_button_text))
                tvHomeSelectRegion3.setTextColor(requireContext().getColor(R.color.unable_button_text))
                ivHomeMoreBtn.setImageResource(R.drawable.ic_more)
                viewControlSpinner.isVisible = false
                val intent = Intent(context, InterestRegionSelectActivity::class.java)
                resultLauncher.launch(intent)
            }

            tvHomeSelectRegion1.setOnClickListener {
                tvHomeCurrentRegion.text = tvHomeSelectRegion1.text
                tvHomeSelectRegion1.setTextColor(requireContext().getColor(R.color.point_color))
                tvHomeSelectRegion2.setTextColor(requireContext().getColor(R.color.unable_button_text))
                tvHomeSelectRegion3.setTextColor(requireContext().getColor(R.color.unable_button_text))
                regionPrefRepository.saveSelectedRegion(1)
            }

            tvHomeSelectRegion2.setOnClickListener {
                tvHomeCurrentRegion.text = tvHomeSelectRegion2.text
                tvHomeSelectRegion2.setTextColor(requireContext().getColor(R.color.point_color))
                tvHomeSelectRegion1.setTextColor(requireContext().getColor(R.color.unable_button_text))
                tvHomeSelectRegion3.setTextColor(requireContext().getColor(R.color.unable_button_text))
                regionPrefRepository.saveSelectedRegion(2)
            }

            tvHomeSelectRegion3.setOnClickListener {
                tvHomeCurrentRegion.text = tvHomeSelectRegion3.text
                tvHomeSelectRegion3.setTextColor(requireContext().getColor(R.color.point_color))
                tvHomeSelectRegion1.setTextColor(requireContext().getColor(R.color.unable_button_text))
                tvHomeSelectRegion2.setTextColor(requireContext().getColor(R.color.unable_button_text))
                regionPrefRepository.saveSelectedRegion(3)
            }

            viewControlSpinner.setOnClickListener {
                clHomeRegionList.isVisible = false
                ivHomeMoreBtn.setImageResource(R.drawable.ic_more)
                viewControlSpinner.isVisible = false
            }

            ivNotification.setOnClickListener {
                // 공지사항 화면으로 이동하는 코드를 여기에 작성하세요.
            }

            ivSearch.setOnClickListener {
                val searchText = etSearch.text.toString()
                performSearch(searchText)
            }

            etSearch.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val searchText = v.text.toString()
                    performSearch(searchText)
                    true
                } else {
                    false
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

    @SuppressLint("ClickableViewAccessibility")
    private fun settingPopupWindow() {
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

    private fun setupBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // RecyclerView가 보일 때만 ViewPager, TabLayout을 보이게 하고, RecyclerView를 숨김
                if (binding.rvSearchResults.visibility == View.VISIBLE) {

                    // 뒤로 가기 버튼을 누를 때 cl_home_region_list를 숨깁니다.
                    binding.clHomeRegionList.isVisible = false

                    binding.viewPager.visibility = View.VISIBLE
                    binding.tabLayout.visibility = View.VISIBLE
                    binding.rvSearchResults.visibility = View.GONE
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })
    }

    private fun setupViewPager() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

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
                1 -> tab.text = "교육강좌"
                2 -> tab.text = "문화행사"
                3 -> tab.text = "시설대관"
                4 -> tab.text = "진료복지"
            }
        }.attach()
    }

    private fun settingRegions(): String {
        val selectedRegions = regionPrefRepository.load().toMutableList()

        return if (selectedRegions.isNotEmpty()) {
            regionPrefRepository.saveSelectedRegion(1)
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

    private fun performSearch(query: String) = lifecycleScope.launch{
        searchPrefRepository.save(query)
        Log.d("Search", "Saved search query: $query") // 로그 찍기

        // searchText 메소드를 호출하여 검색 결과를 가져옴
        val searchResults = reservationRepository.searchText(query)

        // 검색 결과를 HomeSearchAdapter에 전달하여 RecyclerView에 표시
        val adapter = HomeSearchAdapter(searchResults)
        binding.rvSearchResults.adapter = adapter
        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())

        // 검색을 수행할 때 cl_home_region_list를 숨깁니다.
        binding.clHomeRegionList.isVisible = false

        // tv_service_list, tab_layout, view_pager를 숨김
        binding.tvServiceList.visibility = View.GONE
        binding.tabLayout.visibility = View.GONE
        binding.viewPager.visibility = View.GONE

        // 검색 결과를 표시하는 RecyclerView를 보이게 함
        binding.rvSearchResults.visibility = View.VISIBLE

        // 키보드 숨기기
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }
}