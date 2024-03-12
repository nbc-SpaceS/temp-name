package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepository
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentRecommendationBinding
import com.wannabeinseoul.seoulpublicservice.detail.DetailFragment
import com.wannabeinseoul.seoulpublicservice.pref.RecommendPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RecommendPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.ui.recommendation.RecommendationViewModel.Companion.factory
import kotlinx.coroutines.runBlocking

class RecommendationFragment : Fragment() {

    private lateinit var binding: FragmentRecommendationBinding
    private lateinit var viewModel: RecommendationViewModel
    private lateinit var reservationRepository: ReservationRepository
    private lateinit var regionRepository: RegionPrefRepository


    private val app by lazy { requireActivity().application as SeoulPublicServiceApplication }
    private val dbMemoryRepository by lazy { app.container.dbMemoryRepository }
    private val serviceRepository by lazy { app.container.serviceRepository }


    private val showDetailFragment: (RecommendationData) -> Unit =
        { recommendationData: RecommendationData ->
            // RecommendationData에서 svcid를 추출하여 사용
            DetailFragment.newInstance(recommendationData.svcid)
                .show(requireActivity().supportFragmentManager, "Detail")
        }
    private val onItemClick: (RecommendationData) -> Unit =
        { recommendationData: RecommendationData ->
            showDetailFragment(recommendationData)
        }

    private val mainList2 by lazy {
        val selectedAreas = regionRepository.load() // 선택된 지역 목록 가져오기
        val recommendationViews = mutableListOf<RecommendationAdapter.MultiView>()

        selectedAreas.forEach { area ->
            val recommendationDataList =
                dbMemoryRepository.getFiltered(areanm = listOf(area)).take(5).map { row ->
                    row.convertToRecommendationData().apply {
                        runBlocking {
                            reviewCount =
                                serviceRepository.getServiceReviewsCount(row.svcid) + 3
                        }
                    }
                }.toMutableList()

            val horizontalAdapter =
                RecommendationHorizontalAdapter(recommendationDataList, showDetailFragment)
            val horizontalView =
                RecommendationAdapter.MultiView.Horizontal("$area 에 있는 추천 서비스", horizontalAdapter)
            recommendationViews.add(horizontalView)
        }

        recommendationViews.apply {
            // recommendationViews에 새로운 요소를 추가
            add(RecommendationAdapter.MultiView.Tip("그거 아시나요?", "레몬 한개에는 레몬 한개의 비타민이 있습니다."))

        }
    }


    //    private var aa = List(4) {
//        RecommendationAdapter
//            .MultiView.Horizontal(
//                "",
//                RecommendationHorizontalAdapter(
//                    emptyList<RecommendationData>().toMutableList(),
//                    showDetailFragment
//                )
//            )
//
//    }


//    private val mainList2 by lazy {
//        val selectedAreas = regionRepository.load() // 선택된 지역 목록 가져오기
//        val recommendationViews = mutableListOf<RecommendationAdapter.MultiView>()
//
//
//        mutableListOf(
//            RecommendationAdapter.MultiView.Horizontal(
//                "",
//                RecommendationHorizontalAdapter(
//                    emptyList<RecommendationData>().toMutableList(),
//                    showDetailFragment
//                )
//            ),
//            RecommendationAdapter.MultiView.Tip("그거 아시나요?", "레몬 한개에는 레몬 한개의 비타민이 있습니다."),//수정 할 예정.
//
//            RecommendationAdapter.MultiView.Horizontal(
//                "", RecommendationHorizontalAdapter(
//                    emptyList<RecommendationData>().toMutableList(),
//                    showDetailFragment
//                )
//            ),
//            RecommendationAdapter.MultiView.Horizontal(
//                "", RecommendationHorizontalAdapter(
//                    emptyList<RecommendationData>().toMutableList(),
//                    showDetailFragment
//                )
//            ),
//            RecommendationAdapter.MultiView.Horizontal(
//                "", RecommendationHorizontalAdapter(
//                    emptyList<RecommendationData>().toMutableList(),
//                    showDetailFragment
//                )
//            )
//        )
//    }

    private val recommendationAdapter by lazy { RecommendationAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this, factory).get(RecommendationViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        regionRepository = RegionPrefRepositoryImpl(requireContext())
        initView()
        initViewModel()


    }

    private fun initView() {

        binding.reScroll.adapter = recommendationAdapter
        binding.reScroll.layoutManager = LinearLayoutManager(requireContext())
    }


    private fun initViewModel() {

//            viewModel.getList("송파구", 0)
//            viewModel.getList("송파구", 1)
//        viewModel.getList("송파구", 2)
//        viewModel.getList("송파구", 3)
            val regionRepository = RegionPrefRepositoryImpl(requireContext())
            // 선택된 지역 가져오기
            val selectedAreas = regionRepository.load()

            // 선택된 지역별로 추천 서비스 리스트 가져오기
            selectedAreas.forEachIndexed { index, area ->
                viewModel.getList(area, index)


                // 추천 서비스 데이터 업데이트 시 처리
                viewModel.firstRecommendation.observe(viewLifecycleOwner) { recommendationDataList ->
                    if (recommendationDataList.isNotEmpty()) {
                        // 해당 지역의 추천 서비스 어댑터 업데이트
                        val horizontalAdapter = RecommendationHorizontalAdapter(
                            recommendationDataList.toMutableList(),
                            showDetailFragment

                        )
                        // 해당 지역의 MultiView를 업데이트
                        mainList2[index] =
                            RecommendationAdapter.MultiView.Horizontal(
                                "$area 에 있는 추천 서비스",
                                horizontalAdapter

                            )
                        // recommendationAdapter에 업데이트된 mainList2를 제출
                        recommendationAdapter.submitList(mainList2.toMutableList())
                    }
                }


                viewModel.secondRecommendation.observe(viewLifecycleOwner) { recommendationDataList ->
                    if (recommendationDataList.isNotEmpty()) {
                        // 해당 지역의 추천 서비스 어댑터 업데이트
                        val horizontalAdapter = RecommendationHorizontalAdapter(
                            recommendationDataList.toMutableList(),
                            showDetailFragment

                        )
                        // 해당 지역의 MultiView를 업데이트
                        mainList2[index] =
                            RecommendationAdapter.MultiView.Horizontal(
                                "청소년들을 대상으로 하는 추천 서비스",
                                horizontalAdapter

                            )
                        // recommendationAdapter에 업데이트된 mainList2를 제출
                        recommendationAdapter.submitList(mainList2.toMutableList())
                    }
                }

                viewModel.thirdRecommendation.observe(viewLifecycleOwner) { recommendationDataList ->
                    if (recommendationDataList.isNotEmpty()) {
                        // 해당 지역의 추천 서비스 어댑터 업데이트
                        val horizontalAdapter = RecommendationHorizontalAdapter(
                            recommendationDataList.toMutableList(),
                            showDetailFragment

                        )
                        // 해당 지역의 MultiView를 업데이트
                        mainList2[index] =
                            RecommendationAdapter.MultiView.Horizontal(
                                "장애인들을 대상으로 하는 추천 서비스",
                                horizontalAdapter

                            )
                        // recommendationAdapter에 업데이트된 mainList2를 제출
                        recommendationAdapter.submitList(mainList2.toMutableList())
                    }
                }
                viewModel.forthRecommendation.observe(viewLifecycleOwner) { recommendationDataList ->
                    if (recommendationDataList.isNotEmpty()) {
                        // 해당 지역의 추천 서비스 어댑터 업데이트
                        val horizontalAdapter = RecommendationHorizontalAdapter(
                            recommendationDataList.toMutableList(),
                            showDetailFragment

                        )
                        // 해당 지역의 MultiView를 업데이트
                        mainList2[index] =
                            RecommendationAdapter.MultiView.Horizontal(
                                "다음주부터 사용가능한 추천 서비스",
                                horizontalAdapter

                            )
                        // recommendationAdapter에 업데이트된 mainList2를 제출
                        recommendationAdapter.submitList(mainList2.toMutableList())
                    }
                }
            }
        }
    }

