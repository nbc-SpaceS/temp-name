package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.databases.ReservationDAO
import com.wannabeinseoul.seoulpublicservice.databases.ReservationDatabase
import com.wannabeinseoul.seoulpublicservice.databases.ReservationRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentRecommendationBinding
import com.wannabeinseoul.seoulpublicservice.databinding.MyPageItemRecommendedBinding
import com.wannabeinseoul.seoulpublicservice.databinding.RecommendationItemBinding
import com.wannabeinseoul.seoulpublicservice.pref.RecommendPrefRepository
import com.wannabeinseoul.seoulpublicservice.pref.RecommendPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.pref.RowPrefRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulApiService
import com.wannabeinseoul.seoulpublicservice.seoul.SeoulPublicRepositoryImpl
import com.wannabeinseoul.seoulpublicservice.ui.recommendation.RecommendationViewModel.Companion.factory
import com.wannabeinseoul.seoulpublicservice.usecase.GetAll2000UseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecommendationFragment : Fragment() {

    private lateinit var binding: FragmentRecommendationBinding
    private lateinit var viewModel: RecommendationViewModel
    private lateinit var recommendPrefRepository: RecommendPrefRepository
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
    }
}
//override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//    super.onViewCreated(view, savedInstanceState)
//    binding.reScroll.layoutManager = LinearLayoutManager(requireContext())
//
//    viewModel.recommendations.observe(viewLifecycleOwner) { recommendations ->
//        // Set up RecyclerView with the adapter
//        val adapter = RecommendationAdapter(recommendations)
//        binding.reScroll.adapter = adapter
//
//        // RecyclerView의 어댑터에 데이터를 제출합니다.
//        adapter.submitList(recommendations)
//    }
//    viewModel.fetchRecommendations()
//
//    // 기타 작업 수행
//    val loadedData = recommendPrefRepository.load()
//    println(loadedData)
//}



//    private var _binding: FragmentRecommendationBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var viewModel: RecommendationViewModel
//    private lateinit var recommendationAdapter: RecommendationAdapter
//    private lateinit var reservationDAO: ReservationDAO
//    private lateinit var getAll2000UseCase: GetAll2000UseCase
//    private lateinit var recommendPrefRepository: RecommendPrefRepository
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)
//
//        reservationDAO = ReservationDatabase.getDatabase(requireContext()).getReservation()
//
//        recommendPrefRepository = RecommendPrefRepository(requireContext())
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initView()
//        initViewModel()
//        initRecyclerView()
//
//    }
//
//    private fun initView() = binding.let { b ->
//        b.reScroll.layoutManager =
//            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
//    }
//
//    private fun initViewModel() {
//        // ReservationRepository 생성
//        val reservationRepository = ReservationRepositoryImpl(reservationDAO)
//
//        // RecommendationViewModelFactory를 사용하여 ViewModel 초기화
//        viewModel = ViewModelProvider(
//            this,
//            RecommendationViewModelFactory(
//                reservationRepository,
//                recommendPrefRepository,
//                reservationDAO,
//                getAll2000UseCase,
//
//            )
//        )[RecommendationViewModel::class.java]
//
//        // fetchData() 호출하여 데이터 가져오기
//        viewModel.fetchData()
//
//        // LiveData를 관찰하여 UI 업데이트
//        viewModel.regionServices.observe(viewLifecycleOwner) { regionServices ->
//            val convertedRegionServices = regionServices.map { sealedMulti ->
//                when (sealedMulti) {
//                    is SealedMulti.Recommendation -> {
//                        RecommendMultiView.AreaRecommendation(sealedMulti)
//                    }
//
//                    else -> throw IllegalArgumentException("Unknown type of SealedMulti: $sealedMulti")
//                }
//            }
//            recommendationAdapter.setItems(convertedRegionServices)
//        }
//            viewModel.disabilityServices.observe(viewLifecycleOwner) { disabilityServices ->
//                val convertedDisabilityServices = disabilityServices.map { sealedMulti ->
//                    when (sealedMulti) {
//                        is SealedMulti.Recommendation -> {
//                            RecommendMultiView.DisabledRecommendation(sealedMulti)
//                        }
//
//                        else -> throw IllegalArgumentException("Unknown type of SealedMulti: $sealedMulti")
//                    }
//                }
//                recommendationAdapter.setItems(convertedDisabilityServices)
//            }
//
//            viewModel.teenagerServices.observe(viewLifecycleOwner) { teenagerServices ->
//                val convertedTeenagerServices = teenagerServices.map { sealedMulti ->
//                    when (sealedMulti) {
//                        is SealedMulti.Recommendation -> {
//                            RecommendMultiView.TeenagerRecommendation(sealedMulti)
//                        }
//
//                        else -> throw IllegalArgumentException("Unknown type of SealedMulti: $sealedMulti")
//                    }
//                }
//                recommendationAdapter.setItems(convertedTeenagerServices)
//            }
//
//            viewModel.nextWeekServices.observe(viewLifecycleOwner) { nextWeekServices ->
//                val convertedNextWeekServices = nextWeekServices.map { sealedMulti ->
//                    when (sealedMulti) {
//                        is SealedMulti.Recommendation -> {
//                            RecommendMultiView.NextWeekRecommendation(sealedMulti)
//                        }
//
//                        else -> throw IllegalArgumentException("Unknown type of SealedMulti: $sealedMulti")
//                    }
//                }
//                recommendationAdapter.setItems(convertedNextWeekServices)
//            }
//        }
//
//
//    private fun initRecyclerView() {
//        // RecyclerView 설정
//        recommendationAdapter = RecommendationAdapter()
//        binding.reScroll.layoutManager =
//            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
//        binding.reScroll.adapter = recommendationAdapter
//
//        binding.reArea.layoutManager =
//            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        binding.reArea.adapter = RecommendationAdapter()
//
//        binding.reDisabled.layoutManager =
//            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        binding.reDisabled.adapter = RecommendationAdapter()
//
//        binding.reNextWeek.layoutManager =
//            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        binding.reNextWeek.adapter = RecommendationAdapter()
//
//        Log.d("RecommendationFragment", "RecyclerView initialized")
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}



//class RecommendationFragment : Fragment() {
//    private var _binding: FragmentRecommendationBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var viewModel: RecommendationViewModel
//    private lateinit var recommendationAdapter: RecommendationAdapter
//    private lateinit var reservationDAO: ReservationDAO
//    private lateinit var getAll2000UseCase: GetAll2000UseCase
//    private lateinit var recommendPrefRepository: RecommendPrefRepository
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)
//
//        reservationDAO = ReservationDatabase.getDatabase(requireContext()).getReservation()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://openapi.seoul.go.kr:8088")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val seoulApiService = retrofit.create(SeoulApiService::class.java)
//
//        val seoulPublicRepository = SeoulPublicRepositoryImpl(seoulApiService)
//        recommendPrefRepository = RecommendPrefRepository(requireContext())
//
//        val rowPrefRepository = RowPrefRepositoryImpl(requireContext())
//        getAll2000UseCase =
//            GetAll2000UseCase(seoulPublicRepository, recommendPrefRepository, rowPrefRepository)
//
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        initView()
//        initViewModel()
//        initRecyclerView()
//    }
//
//    private fun initView() = binding.let { b ->
//        b.reScroll.layoutManager =
//            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
//    }
//
//    private fun initViewModel() {
//        // ReservationRepository 생성
//        val reservationRepository = ReservationRepositoryImpl(reservationDAO)
//
//        // RecommendationViewModelFactory를 사용하여 ViewModel 초기화
//        viewModel = ViewModelProvider(
//            this,
//            RecommendationViewModelFactory(
//                reservationRepository,
//                recommendPrefRepository,
//                reservationDAO,
//                getAll2000UseCase
//            )
//        ).get(RecommendationViewModel::class.java)
//
//        // fetchData() 호출하여 데이터 가져오기
//        viewModel.fetchData()
//
//        // LiveData를 관찰하여 UI 업데이트
//        viewModel.regionServices.observe(viewLifecycleOwner) { regionServices ->
//            val convertedRegionServices = regionServices.map { sealedMulti ->
//                when (sealedMulti) {
//                    is SealedMulti.Recommendation -> {
//                        RecommendMultiView.AreaRecommendation(sealedMulti)
//                    }
//                    else -> throw IllegalArgumentException("Unknown type of SealedMulti: $sealedMulti")
//                }
//            }
//            recommendationAdapter.setItems(convertedRegionServices)
//        }
//
//        viewModel.disabilityServices.observe(viewLifecycleOwner) { disabilityServices ->
//            val convertedDisabilityServices = disabilityServices.map { sealedMulti ->
//                when (sealedMulti) {
//                    is SealedMulti.Recommendation -> {
//                        RecommendMultiView.DisabledRecommendation(sealedMulti)
//                    }
//                    else -> throw IllegalArgumentException("Unknown type of SealedMulti: $sealedMulti")
//                }
//            }
//            recommendationAdapter.setItems(convertedDisabilityServices)
//        }
//
//        viewModel.teenagerServices.observe(viewLifecycleOwner) { teenagerServices ->
//            val convertedTeenagerServices = teenagerServices.map { sealedMulti ->
//                when (sealedMulti) {
//                    is SealedMulti.Recommendation -> {
//                        RecommendMultiView.TeenagerRecommendation(sealedMulti)
//                    }
//                    else -> throw IllegalArgumentException("Unknown type of SealedMulti: $sealedMulti")
//                }
//            }
//            recommendationAdapter.setItems(convertedTeenagerServices)
//        }
//
//        viewModel.nextWeekServices.observe(viewLifecycleOwner) { nextWeekServices ->
//            val convertedNextWeekServices = nextWeekServices.map { sealedMulti ->
//                when (sealedMulti) {
//                    is SealedMulti.Recommendation -> {
//                        RecommendMultiView.NextWeekRecommendation(sealedMulti)
//                    }
//                    else -> throw IllegalArgumentException("Unknown type of SealedMulti: $sealedMulti")
//                }
//            }
//            recommendationAdapter.setItems(convertedNextWeekServices)
//        }
//    }
//
//    private fun initRecyclerView() {
//        // RecyclerView 설정
//        recommendationAdapter = RecommendationAdapter()
//        binding.reScroll.layoutManager =
//            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
//        binding.reScroll.adapter = recommendationAdapter
//
//        binding.reArea.layoutManager =
//            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        binding.reArea.adapter = RecommendationAdapter()
//
//        binding.reDisabled.layoutManager =
//            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        binding.reDisabled.adapter = RecommendationAdapter()
//
//        binding.reNextWeek.layoutManager =
//            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
//        binding.reNextWeek.adapter = RecommendationAdapter()
//
//        Log.d("RecommendationFragment", "RecyclerView initialized")
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}