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
//눌렀을때 쇼디테일 가도록 할 예정.

    private val horizontalAdapters = List(4) {
        RecommendationHorizontalAdapter(
            emptyList<RecommendationData>().toMutableList(), showDetailFragment
        )
    }

    private val horizontals = List(4) {
        RecommendationAdapter.MultiView.Horizontal(
            "1234", horizontalAdapters[it]
        )
    }

//    private val mainList2 by lazy {
//        val selectedAreas = regionRepository.load() // 선택된 지역 목록 가져오기
//        val recommendationViews = mutableListOf<RecommendationAdapter.MultiView>()
//
//        selectedAreas.forEach { area ->
//            val recommendationDataList =
//                dbMemoryRepository.getFiltered(areanm = listOf(area)).take(5).map { row ->
//                    row.convertToRecommendationData().apply {
//                        runBlocking {
//                            reviewCount =
//                                serviceRepository.getServiceReviewsCount(row.svcid) + 3
//                        }
//                    }
//                }.toMutableList()
//
//            val horizontalAdapter =
//                RecommendationHorizontalAdapter(recommendationDataList, showDetailFragment)
//            val horizontalView =
//                RecommendationAdapter.MultiView.Horizontal("$area 에 있는 추천 서비스", horizontalAdapter)
//            recommendationViews.add(horizontalView)
//        }
//
//        recommendationViews.apply {
//            // recommendationViews에 새로운 요소를 추가
//            add(RecommendationAdapter.MultiView.Tip("그거 아시나요?", "레몬 한개에는 레몬 한개의 비타민이 있습니다."))
//
//        }
//    }


    private val recommendationAdapter by lazy {
        RecommendationAdapter().apply {
            submitList(
                listOf(
                    horizontals[0],
                    horizontals[1],
                    RecommendationAdapter.MultiView.Tip(
                        "그거 아시나요?", "이 앱에는 누군가의 피 땀 눈물이 들어있다는 것을"
                    ),
                    horizontals[2],
                    horizontals[3],
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

    private fun initView() = binding.let { binding ->

        binding.reScroll.adapter = recommendationAdapter
        binding.reScroll.layoutManager = LinearLayoutManager(requireContext())
    }


    private fun initViewModel() {

        for ((index, liveData) in viewModel.recommendationListLivedataList.withIndex()) {
            liveData.observe(viewLifecycleOwner) {
                horizontalAdapters.getOrNull(index)?.submitList(it)
            }
        }
    }
}
