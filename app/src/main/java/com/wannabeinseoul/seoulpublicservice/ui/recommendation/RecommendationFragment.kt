package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentRecommendationBinding
import com.wannabeinseoul.seoulpublicservice.detail.DetailFragment
import com.wannabeinseoul.seoulpublicservice.pref.RecommendPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RecommendPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.ui.recommendation.RecommendationViewModel.Companion.factory
import kotlinx.coroutines.runBlocking

class RecommendationFragment : Fragment() {

    private lateinit var binding: FragmentRecommendationBinding
    private lateinit var viewModel: RecommendationViewModel
    private lateinit var recommendPrefRepository: RecommendPrefRepository

    private val app by lazy { requireActivity().application as SeoulPublicServiceApplication }
    private val dbMemoryRepository by lazy { app.container.dbMemoryRepository }
    private val serviceRepository by lazy { app.container.serviceRepository }

    private val showDetailFragment = { svcid: String ->
        DetailFragment.newInstance(svcid)
            .show(requireActivity().supportFragmentManager, "Detail")
    }

    private val mainList by lazy {
        listOf(
            RecommendationAdapter.MultiView.Horizontal("송파구에 있는 추천 서비스",
                RecommendationHorizontalAdapter(
                dbMemoryRepository.getFiltered( areanm = listOf("송파구")).take(5).map { row ->
                    row.convertToRecommendationData().apply {
                        runBlocking { reviewCount = serviceRepository.getServiceReviewsCount(row.svcid) + 3 }//후기
                    }
                }.toMutableList(),
                        showDetailFragment
            )),
            RecommendationAdapter.MultiView.Tip("그거 아시나요?", "레몬 한개에는 레몬 한개의 비타민이 있습니다."),//수정 할 예정.

            RecommendationAdapter.MultiView.Horizontal("청소년들을 대상으로 하는 공공서비스", RecommendationHorizontalAdapter(
                dbMemoryRepository.getFiltered( areanm = listOf("송파구")).take(5).map { row ->
                    row.convertToRecommendationData().apply {
                        runBlocking { reviewCount = serviceRepository.getServiceReviewsCount(row.svcid) + 3 }//후기
                    }
                }.toMutableList(),
                    showDetailFragment
                    )),
            RecommendationAdapter.MultiView.Horizontal("장애인들을 대상으로 하는 공공서비스", RecommendationHorizontalAdapter(
                dbMemoryRepository.getFiltered( areanm = listOf("송파구")).take(5).map { row ->
                    row.convertToRecommendationData().apply {
                        runBlocking { reviewCount = serviceRepository.getServiceReviewsCount(row.svcid) + 3 }//후기
                    }
                }.toMutableList(),
                showDetailFragment
            )),
            RecommendationAdapter.MultiView.Horizontal("다음주부터 사용가능한 공공서비스", RecommendationHorizontalAdapter(
                dbMemoryRepository.getFiltered( areanm = listOf("송파구")).take(5).map { row ->
                    row.convertToRecommendationData().apply {
                        runBlocking { reviewCount = serviceRepository.getServiceReviewsCount(row.svcid) + 3 }//후기
                    }
                }.toMutableList(),
                showDetailFragment
            ))
        )
    }

    private val recommendationAdapter by lazy { RecommendationAdapter(mainList) }

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
        binding.reScroll.layoutManager = LinearLayoutManager(requireContext())

        recommendPrefRepository = RecommendPrefRepositoryImpl(requireContext())

        viewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
            // Set up RecyclerView with the adapter
            val adapter = RecommendationAdapter(recommendations)
            binding.reScroll.adapter = adapter

            // RecyclerView의 어댑터에 데이터를 제출합니다.
            adapter.submitList(recommendations)
        }
        viewModel.fetchRecommendations()

        // 기타 작업 수행
        val loadedData = recommendPrefRepository.load()
        println(loadedData)

        initView()
    }

    private fun initView() {
        binding.reScroll.adapter = recommendationAdapter
    }
}