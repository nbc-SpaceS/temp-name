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

class RecommendationFragment : Fragment() {

    private lateinit var binding: FragmentRecommendationBinding
    private lateinit var viewModel: RecommendationViewModel
    private lateinit var recommendPrefRepository: RecommendPrefRepository

    private val app by lazy { requireActivity().application as SeoulPublicServiceApplication }
    private val dbMemoryRepository by lazy { app.container.dbMemoryRepository }

    private val showDetailFragment = { svcid: String ->
        DetailFragment.newInstance(svcid)
            .show(requireActivity().supportFragmentManager, "Detail")
    }

    private val horizontalAdapter1 by lazy {
        RecommendationHorizontalAdapter(
            dbMemoryRepository.getAll().take(5).convertToRecommendationDataList().toMutableList(),
            showDetailFragment
        )
    }

    private val horizontalAdapter2 by lazy {
        RecommendationHorizontalAdapter(
            dbMemoryRepository.getAll().take(10).convertToRecommendationDataList().toMutableList(),
            showDetailFragment
        )
    }

    private val mainList by lazy {
        listOf(
            RecommendationAdapter.MultiView.Horizontal("송파구에 있는 추천 서비스", horizontalAdapter1),
            RecommendationAdapter.MultiView.Tip("그거 아시나요?", "레몬 한개에는 레몬 한개의 비타민이 있습니다."),
            RecommendationAdapter.MultiView.Horizontal("청소년들을 대상으로 하는 공공서비스", horizontalAdapter2),
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