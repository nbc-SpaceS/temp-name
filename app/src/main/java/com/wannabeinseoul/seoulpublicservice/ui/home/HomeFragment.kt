package com.wannabeinseoul.seoulpublicservice.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentHomeBinding
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.SearchPrefRepository
import com.wannabeinseoul.seoulpublicservice.ui.category.CategoryViewModel
import com.wannabeinseoul.seoulpublicservice.ui.interestregionselect.InterestRegionSelectActivity
import com.wannabeinseoul.seoulpublicservice.ui.main.adapter.HomeSearchAdapter
import com.wannabeinseoul.seoulpublicservice.ui.main.adapter.SearchHistoryAdapter
import com.wannabeinseoul.seoulpublicservice.ui.notifications.NotificationsFragment
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val regionPrefRepository: RegionPrefRepository by lazy { (requireActivity().application as SeoulPublicServiceApplication).container.regionPrefRepository }
    private val searchPrefRepository: SearchPrefRepository by lazy { (requireActivity().application as SeoulPublicServiceApplication).container.searchPrefRepository }
    private val reservationRepository: ReservationRepository by lazy { (requireActivity().application as SeoulPublicServiceApplication).container.reservationRepository }

    private val categoryViewModel: CategoryViewModel by viewModels { CategoryViewModel.factory }

    private var backPressedOnce = false

    private var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            setupRegions(binding)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUIComponents()
    }

    private fun setupUIComponents() {
        setupRegions(binding)
        setupViewPager(binding)
        setupBackPress(binding)
        setupSearch(binding)
        setupSearchHistory(binding)
        setupRootClickListener(binding)
        setupRegionSelection(binding)
        setupNotificationClick(binding)
    }

    private fun setupRegionSelection(binding: FragmentHomeBinding) {
        binding.clHomeSetRegion.setOnClickListener {
            toggleRegionListVisibility(binding)
        }

        binding.tvHomeReSelectRegionBtn.setOnClickListener {
            reselectRegion(binding)
        }

        val regionViews = listOf(
            binding.tvHomeSelectRegion1,
            binding.tvHomeSelectRegion2,
            binding.tvHomeSelectRegion3
        )

        regionViews.forEachIndexed { index, regionView ->
            regionView.setOnClickListener {
                selectRegion(regionView, index + 1, binding, regionViews)
            }
        }

        binding.viewControlSpinner.setOnClickListener {
            toggleRegionListVisibility(binding)
        }
    }

    private fun toggleRegionListVisibility(binding: FragmentHomeBinding) {
        if (binding.clHomeRegionList.isVisible) {
            binding.ivHomeMoreBtn.setImageResource(R.drawable.ic_more)
            binding.viewControlSpinner.isVisible = false
        } else {
            binding.ivHomeMoreBtn.setImageResource(R.drawable.ic_less)
            binding.viewControlSpinner.isVisible = true
        }
        binding.clHomeRegionList.isVisible = !binding.clHomeRegionList.isVisible
    }

    private fun reselectRegion(binding: FragmentHomeBinding) {
        binding.clHomeRegionList.isVisible = false
        binding.tvHomeSelectRegion1.setTextColor(requireContext().getColor(R.color.unable_button_text))
        binding.tvHomeSelectRegion2.setTextColor(requireContext().getColor(R.color.unable_button_text))
        binding.tvHomeSelectRegion3.setTextColor(requireContext().getColor(R.color.unable_button_text))
        binding.ivHomeMoreBtn.setImageResource(R.drawable.ic_more)
        binding.viewControlSpinner.isVisible = false
        val intent = Intent(context, InterestRegionSelectActivity::class.java)
        resultLauncher.launch(intent)
    }

    private fun selectRegion(regionView: TextView, index: Int, binding: FragmentHomeBinding, regionViews: List<TextView>) {
        regionViews.forEach { view ->
            if (view == regionView) {
                view.setTextColor(requireContext().getColor(R.color.point_color))
            } else {
                view.setTextColor(requireContext().getColor(R.color.unable_button_text))
            }
        }

        binding.tvHomeCurrentRegion.text = regionView.text
        regionPrefRepository.saveSelectedRegion(index)
    }

    private fun setupNotificationClick(binding: FragmentHomeBinding) {
        binding.ivNotification.setOnClickListener {
            // 공지사항 화면으로 이동하는 코드를 여기에 작성하세요.
            val notificationFragment = NotificationsFragment.newInstance()
            notificationFragment.show(requireActivity().supportFragmentManager, "NotificationFragment")
        }
    }

    private fun setupBackPress(binding: FragmentHomeBinding) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // RecyclerView가 보일 때만 ViewPager, TabLayout을 보이게 하고, RecyclerView를 숨김
                if (binding.rvSearchResults.visibility == View.VISIBLE) {

                    // 뒤로 가기 버튼을 누를 때 cl_home_region_list를 숨깁니다.
                    binding.clHomeRegionList.isVisible = false
                    binding.tvServiceList.visibility = View.VISIBLE
                    binding.viewPager.visibility = View.VISIBLE
                    binding.tabLayout.visibility = View.VISIBLE
                    binding.rvSearchResults.visibility = View.GONE
                } else if (backPressedOnce) {
                    isEnabled = false
                    requireActivity().finish()
                } else {
                    backPressedOnce = true
                    Toast.makeText(requireContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        backPressedOnce = false
                    }, 2000)
                }
            }
        })
    }

    private fun setupViewPager(binding: FragmentHomeBinding) {
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

    private fun setupRegions(binding: FragmentHomeBinding): String {
        val selectedRegions = regionPrefRepository.load().toMutableList()

        return if (selectedRegions.isNotEmpty()) {
            regionPrefRepository.saveSelectedRegion(1)
            updateUIWithSelectedRegions(binding, selectedRegions)
        } else {
            updateUIWithNoSelectedRegions(binding)
        }
    }

    private fun updateUIWithSelectedRegions(binding: FragmentHomeBinding, selectedRegions: List<String>): String {
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
        return selectedRegions[0]
    }

    private fun updateUIWithNoSelectedRegions(binding: FragmentHomeBinding): String {
        binding.tvHomeCurrentRegion.text = "지역선택"
        binding.tvHomeSelectRegion1.isVisible = false
        binding.tvHomeSelectRegion2.isVisible = false
        binding.tvHomeSelectRegion3.isVisible = false
        return "지역선택"
    }

    private fun performSearch(query: String) = lifecycleScope.launch{
        // 검색어가 비어있지 않을 때만 검색어가 저장됨
        if (query.isNotEmpty()) {
            saveSearchQuery(query)
        }

        displaySearchResults(query)
    }

    private fun saveSearchQuery(query: String) {
        searchPrefRepository.save(query)
        Log.d("Search", "Saved search query: $query") // 로그 찍기

        // 키보드 숨기기
        hideKeyboard(binding)

        // EditText의 포커스 제거
        binding.etSearch.clearFocus()
    }

    private suspend fun displaySearchResults(query: String) {
        // searchText 메소드를 호출하여 검색 결과를 가져옴
        val searchResults = reservationRepository.searchText(query)

        // 검색 결과를 HomeSearchAdapter에 전달하여 RecyclerView에 표시
        val adapter = HomeSearchAdapter(searchResults)
        binding.rvSearchResults.adapter = adapter
        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())

        // 검색을 수행할 때 cl_home_region_list를 숨김
        binding.clHomeRegionList.isVisible = false

        // tv_service_list, tab_layout, view_pager를 숨김
        binding.tvServiceList.visibility = View.GONE
        binding.tabLayout.visibility = View.GONE
        binding.viewPager.visibility = View.GONE

        // 검색 결과를 표시하는 RecyclerView를 보이게 함
        binding.rvSearchResults.visibility = View.VISIBLE

        // 키보드 숨기기
        hideKeyboard(binding)
    }
    private fun setupSearch(binding: FragmentHomeBinding) {
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
    }

    private fun setupSearchHistory(binding: FragmentHomeBinding) {
        binding.etSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showSearchHistory(binding)
            } else {
                hideSearchHistory(binding)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupRootClickListener(binding: FragmentHomeBinding) {
        binding.root.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // 터치 이벤트가 발생하면 포커스를 해제
                binding.etSearch.clearFocus()
            }
            true
        }
    }

    private fun showSearchHistory(binding: FragmentHomeBinding) {
        // 포커스가 EditText에 있을 때
        // 저장된 검색어를 불러옴
        val searchHistory = searchPrefRepository.load().toMutableList()

        // 검색어를 SearchHistoryAdapter에 전달하여 RecyclerView에 표시
        val adapter = SearchHistoryAdapter(searchHistory, searchPrefRepository).apply {
            onItemClickListener = object : SearchHistoryAdapter.OnItemClickedListener {
                override fun onItemClick(item: String) {
                    binding.etSearch.setText(item)
                    performSearch(item)
                }
            }
        }
        binding.rvSearchHistory.adapter = adapter
        binding.rvSearchHistory.layoutManager = LinearLayoutManager(requireContext())

        // 검색어 저장 목록을 표시하는 RecyclerView를 보이게 함
        binding.rvSearchHistory.visibility = View.VISIBLE
    }

    private fun hideSearchHistory(binding: FragmentHomeBinding) {
        // 포커스가 EditText에서 벗어났을 때
        // 검색어 저장 목록을 표시하는 RecyclerView를 숨김
        binding.rvSearchHistory.visibility = View.GONE
    }

    private fun hideKeyboard(binding: FragmentHomeBinding) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

      //아직 구현하지 못한 내용
//    private fun checkNotification() {
//        val sharedPreferences = requireContext().getSharedPreferences("SavedPrefRepository", Context.MODE_PRIVATE)
//        val gson = Gson()
//        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
//            if (key == "keyNotificationSvcidList") {
//                val json = sharedPreferences.getString(key, null) ?: ""
//                val result = gson.fromJson(json, Array<String>::class.java).toList()
//                Log.d("dkj4", "$result")
//                binding.ivHomeNotificationCountBackground.isVisible = result.isNotEmpty()
//            }
//        }
//        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
//    }
}