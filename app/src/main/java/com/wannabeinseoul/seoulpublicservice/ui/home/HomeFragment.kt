package com.wannabeinseoul.seoulpublicservice.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.tabs.TabLayoutMediator
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentHomeBinding
import com.wannabeinseoul.seoulpublicservice.ui.category.CategoryItemClick
import com.wannabeinseoul.seoulpublicservice.ui.detail.DetailFragment
import com.wannabeinseoul.seoulpublicservice.ui.interestregionselect.InterestRegionSelectActivity
import com.wannabeinseoul.seoulpublicservice.ui.main.MainViewModel
import com.wannabeinseoul.seoulpublicservice.ui.main.adapter.HomeSearchAdapter
import com.wannabeinseoul.seoulpublicservice.ui.main.adapter.SearchHistoryAdapter
import com.wannabeinseoul.seoulpublicservice.ui.notifications.NotificationsFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels { HomeViewModel.factory }
    private val mainViewModel: MainViewModel by activityViewModels()

    private var backPressedOnce = false

    private var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            viewModel.setupRegions()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUIComponents()
        initViewModel()
    }

    private fun initViewModel() = with(viewModel) {
        updateSelectedRegions.observe(viewLifecycleOwner) { selectedRegions ->
            with(binding) {
                if (selectedRegions.isEmpty()) {
                    tvHomeCurrentRegion.text = "지역선택"
                    tvHomeSelectRegion1.isVisible = false
                    tvHomeSelectRegion2.isVisible = false
                    tvHomeSelectRegion3.isVisible = false
                    mdHomeRegionList.isVisible = false
                    mainViewModel.setRegion("지역선택")
                } else {
                    tvHomeCurrentRegion.text = selectedRegions[0]
                    tvHomeSelectRegion1.setTextColor(requireContext().getColor(R.color.total_text_color))
                    mdHomeRegionList.isVisible = true
                    mainViewModel.setRegion(selectedRegions[0])
                    when (selectedRegions.size) {
                        1 -> {
                            tvHomeSelectRegion1.text = selectedRegions[0]
                            tvHomeSelectRegion1.isVisible = true
                            tvHomeSelectRegion2.isVisible = false
                            tvHomeSelectRegion3.isVisible = false
                        }

                        2 -> {
                            tvHomeSelectRegion1.text = selectedRegions[0]
                            tvHomeSelectRegion2.text = selectedRegions[1]
                            tvHomeSelectRegion1.isVisible = true
                            tvHomeSelectRegion2.isVisible = true
                            tvHomeSelectRegion3.isVisible = false
                        }

                        3 -> {
                            tvHomeSelectRegion1.text = selectedRegions[0]
                            tvHomeSelectRegion2.text = selectedRegions[1]
                            tvHomeSelectRegion3.text = selectedRegions[2]
                            tvHomeSelectRegion1.isVisible = true
                            tvHomeSelectRegion2.isVisible = true
                            tvHomeSelectRegion3.isVisible = true
                        }
                    }
                }
            }
        }

        displaySearchResult.observe(viewLifecycleOwner) { searchResult ->
            with(binding) {
                if (searchResult.isNotEmpty()) {
                    // 검색 결과를 HomeSearchAdapter에 전달하여 RecyclerView에 표시
                    val adapter = HomeSearchAdapter(searchResult)
                    rvSearchResults.adapter = adapter
                    rvSearchResults.layoutManager = LinearLayoutManager(requireContext())

                    // 검색을 수행할 때 cl_home_region_list를 숨김
                    clHomeRegionList.isVisible = false

                    // tv_service_list, tab_layout, view_pager를 숨김
                    tvServiceList.visibility = View.GONE
                    tabLayout.visibility = View.GONE
                    viewPager.visibility = View.GONE

                    // 검색 결과를 표시하는 RecyclerView를 보이게 함
                    rvSearchResults.visibility = View.VISIBLE

                    // 키보드 숨기기
                    hideKeyboard()

                    // et_search 포커스 제거
                    etSearch.clearFocus()

                    // 클릭된 결과 아이템의 SVCID를 상세 페이지에 전달
                    searchClick(adapter)
                }
            }
        }

        displaySearchHistory.observe(viewLifecycleOwner) { searchHistory ->
            with(binding) {
                if (searchHistory.first.isNotEmpty()) {
                    // 검색어를 SearchHistoryAdapter에 전달하여 RecyclerView에 표시
                    val adapter = SearchHistoryAdapter(
                        searchHistory.first.toMutableList(),
                        searchHistory.second
                    ).apply {
                        onItemClickListener = object : SearchHistoryAdapter.OnItemClickedListener {
                            override fun onItemClick(item: String) {
                                etSearch.setText(item)
                                viewModel.performSearch(item)
                            }
                        }
                    }
                    rvSearchHistory.adapter = adapter
                    rvSearchHistory.layoutManager = LinearLayoutManager(requireContext())

                    // 검색어 저장 목록을 표시하는 RecyclerView를 보이게 함
                    rvSearchHistory.visibility = View.VISIBLE
                }
            }
        }

        updateViewPagerCategory.observe(viewLifecycleOwner) {
            binding.tvHomeDescription.text = when (it.size) {
                0 -> "${mainViewModel.selectRegion.value}에는 모든 서비스가 최소 1개 이상 있습니다."
                1 -> "${mainViewModel.selectRegion.value}에는 ${it[0].first} 서비스가 없습니다."
                2 -> "${mainViewModel.selectRegion.value}에는 ${it[0].first}, ${it[1].first} 서비스가 없습니다."
                3 -> "${mainViewModel.selectRegion.value}에는 ${it[0].first}, ${it[1].first}, ${it[2].first} 서비스가 없습니다."
                4 -> "${mainViewModel.selectRegion.value}에는 ${it[0].first}, ${it[1].first}, ${it[2].first}, ${it[3].first} 서비스가 없습니다."
                else -> "${mainViewModel.selectRegion.value}에는 사용할 수 있는 서비스가 없습니다."
            }
            binding.tvHomeDescription.text = when (it.size) {
                1 -> setSpannableString(6, 11)
                2 -> setSpannableString(6, 17)
                3 -> setSpannableString(6, 23)
                4 -> setSpannableString(6, 29)
                else -> binding.tvHomeDescription.text
            }
        }
    }

    private fun setupUIComponents() {
        viewModel.setRandomService()
        viewModel.setupRegions()

        setupViewPager()
        setupBackPress()
        setupSearch()
        setupSearchHistory()
        setupRegionSelection()
        setupNotificationClick()
        setupBannerClick()

        mainViewModel.selectRegion.observe(viewLifecycleOwner) {
            if (it != "지역선택") {
                viewModel.setViewPagerCategory(it)
            } else {
                binding.tvHomeDescription.text = "아직 관심지역이 선택되지 않았습니다."
            }
        }
    }

    private fun setupViewPager() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        viewPager.offscreenPageLimit = 2
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 5

            override fun createFragment(position: Int): Fragment {
                val fragment = when (position) {
                    0 -> FacilityFragment()
                    1 -> EducationFragment()
                    2 -> CultureEventFragment()
                    3 -> FacilityRentFragment()
                    4 -> MedicalFragment()
                    else -> Fragment()
                }
                return fragment
            }
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "체육시설"
                1 -> tab.text = "교육강좌"
                2 -> tab.text = "문화체험"
                3 -> tab.text = "시설대관"
                4 -> tab.text = "진료복지"
            }
        }.attach()
    }

    private fun setupBackPress() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
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
                        Toast.makeText(requireContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT)
                            .show()

                        Handler(Looper.getMainLooper()).postDelayed({
                            backPressedOnce = false
                        }, 2000)
                    }
                }
            })
    }

    private fun setupSearch() {
        binding.ivSearch.setOnClickListener {
            val searchText = binding.etSearch.text.toString()
            viewModel.performSearch(searchText)
        }

        binding.etSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchText = v.text.toString()
                viewModel.performSearch(searchText)

                // EditText의 포커스 제거
                binding.etSearch.clearFocus()
                true
            } else {
                false
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupSearchHistory() {
        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.showSearchHistory()
                binding.viewControlRvSearchResults.visibility = View.VISIBLE
            } else {
                hideSearchHistory()
                binding.viewControlRvSearchResults.visibility = View.GONE
            }
        }

        // viewControlRvSearchResults가 터치될 때 호출되는 리스너 설정
        binding.viewControlRvSearchResults.setOnTouchListener { _, _ ->
            // viewControlRvSearchResults가 터치되면 EditText의 포커스를 해제, 키보드 숨기기
            binding.etSearch.clearFocus()
            hideKeyboard()
            true
        }
    }



    private fun setupRegionSelection() {
        binding.clHomeSetRegion.setOnClickListener {
            toggleRegionListVisibility()
        }

        binding.tvHomeReSelectRegionBtn.setOnClickListener {
            reselectRegion()
        }

        val regionViews = listOf(
            binding.tvHomeSelectRegion1,
            binding.tvHomeSelectRegion2,
            binding.tvHomeSelectRegion3
        )

        regionViews.forEachIndexed { index, regionView ->
            regionView.setOnClickListener {
                selectRegion(regionView, index + 1, regionViews)
            }
        }

        binding.viewControlSpinner.setOnClickListener {
            toggleRegionListVisibility()
        }
    }

    private fun setupNotificationClick() {
        binding.ivNotification.setOnClickListener {
            // 공지사항 화면으로 이동하는 코드를 여기에 작성하세요.
            val notificationFragment = NotificationsFragment.newInstance()
            notificationFragment.show(
                requireActivity().supportFragmentManager, "NotificationFragment"
            )
        }
    }

    private fun setupBannerClick() {
        binding.ivHomeMainBanner.setOnClickListener {
            if (viewModel.randomService.isEmpty()) {
                Toast.makeText(requireContext(), "최근에 나온 서비스가 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                val dialog = DetailFragment.newInstance(viewModel.randomService.random())
                dialog.show(requireActivity().supportFragmentManager, "Detail")
            }
        }
    }

    private fun toggleRegionListVisibility() {
        if (binding.clHomeRegionList.isVisible) {
            binding.ivHomeMoreBtn.setImageResource(R.drawable.ic_more)
            binding.viewControlSpinner.isVisible = false
        } else {
            binding.ivHomeMoreBtn.setImageResource(R.drawable.ic_less)
            binding.viewControlSpinner.isVisible = true
        }
        binding.clHomeRegionList.isVisible = !binding.clHomeRegionList.isVisible
    }

    private fun reselectRegion() {
        binding.clHomeRegionList.isVisible = false
        binding.tvHomeSelectRegion1.setTextColor(requireContext().getColor(R.color.unable_button_text))
        binding.tvHomeSelectRegion2.setTextColor(requireContext().getColor(R.color.unable_button_text))
        binding.tvHomeSelectRegion3.setTextColor(requireContext().getColor(R.color.unable_button_text))
        binding.ivHomeMoreBtn.setImageResource(R.drawable.ic_more)
        binding.viewControlSpinner.isVisible = false
        val intent = Intent(context, InterestRegionSelectActivity::class.java)
        resultLauncher.launch(intent)
    }

    private fun selectRegion(regionView: TextView, index: Int, regionViews: List<TextView>) {
        regionViews.forEach { view ->
            if (view == regionView) {
                view.setTextColor(requireContext().getColor(R.color.total_text_color))
                mainViewModel.setRegion(regionView.text.toString())
            } else {
                view.setTextColor(requireContext().getColor(R.color.unable_button_text))
            }
        }

        binding.tvHomeCurrentRegion.text = regionView.text
        viewModel.saveSelectedRegion(index)
    }

    private fun hideSearchHistory() {
        // 포커스가 EditText에서 벗어났을 때 검색어 저장 목록을 표시하는 RecyclerView를 숨김
        binding.rvSearchHistory.visibility = View.GONE
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
    }

    private fun searchClick(adapter: HomeSearchAdapter) {
        adapter.categoryItemClick = object : CategoryItemClick {
            override fun onClick(svcID: String) {
                val dialog = DetailFragment.newInstance(svcID)
                dialog.show(requireActivity().supportFragmentManager, "CategoryFrag")
            }
        }
    }

    private fun setSpannableString(start: Int, end: Int): SpannableString {
        val spannableString = SpannableString(binding.tvHomeDescription.text)

        spannableString.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.total_text_color
                )
            ),
            start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannableString
    }

    override fun onStop() {
        viewModel.clearSearchResult()
        super.onStop()
    }
}