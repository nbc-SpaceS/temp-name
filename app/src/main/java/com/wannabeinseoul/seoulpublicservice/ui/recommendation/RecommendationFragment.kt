package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentRecommendationBinding
import com.wannabeinseoul.seoulpublicservice.ui.detail.DetailFragment
import com.wannabeinseoul.seoulpublicservice.ui.recommendation.RecommendationViewModel.Companion.factory

class RecommendationFragment : Fragment() {

    private lateinit var binding: FragmentRecommendationBinding
    private val viewModel: RecommendationViewModel by viewModels { factory }


    private val app by lazy { requireActivity().application as SeoulPublicServiceApplication }
    private val dbMemoryRepository by lazy { app.container.dbMemoryRepository }
    private val serviceRepository by lazy { app.container.serviceRepository }
    private val regionPrefRepository by lazy { app.container.regionPrefRepository }
    private val reservationRepository by lazy { app.container.reservationRepository }


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

//    private val horizontalAdapters = List(4) {
//        RecommendationHorizontalAdapter(
//            emptyList<RecommendationData>().toMutableList(), showDetailFragment
//        )
//    }

//    private val horizontalAdapters by lazy {
//        viewModel.recommendationListLivedataList.map { liveData ->
//            RecommendationHorizontalAdapter(mutableListOf(), showDetailFragment)
//                .also { adapter ->
//                    liveData.observe(viewLifecycleOwner) { adapter.submitList(it) }
//                }
//        }
//    }


//    private val horizontals by lazy {
//        List(4) {
//            val region = regionPrefRepository.loadSelectedRegion()
//            RecommendationAdapter.MultiView.Horizontal(
//                "$region", horizontalAdapters[it]
//            )
//        }
//    }
//    private val horizontals = List(4) {
//        RecommendationAdapter.MultiView.Horizontal(
//            "1234", horizontalAdapters[it]
//        )
//    }

    private val recommendationAdapter = RecommendationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()

    }

    private fun initView() = binding.let { binding ->
        binding.reScroll.adapter = recommendationAdapter
        binding.reScroll.layoutManager = LinearLayoutManager(requireContext())
    }


    private fun initViewModel() = viewModel.let { vm ->
//        for ((index, liveData) in viewModel.recommendationListLivedataList.withIndex()) {
//            liveData.observe(viewLifecycleOwner) {
//                horizontalAdapters.getOrNull(index)?.submitList(it)
//            }
//        }

        vm.horizontalDataList.observe(viewLifecycleOwner) { horizontalDataList ->
            val multiViews: MutableList<RecommendationAdapter.MultiView> = horizontalDataList.map {
                RecommendationAdapter.MultiView.Horizontal(
                    it.title,
                    RecommendationHorizontalAdapter(mutableListOf(), showDetailFragment)
                        .apply { submitList(it.list) }
                )
            }.toMutableList()
            if (multiViews.size >= 2) multiViews.add(
                2,
                RecommendationAdapter.MultiView.Tip("팁 제목입니다", "팁 내용입니다")
            )
            viewModel.setMultiViews(multiViews)
        }

        vm.multiViews.observe(viewLifecycleOwner) {
            recommendationAdapter.submitList(it)
        }
    }
}
