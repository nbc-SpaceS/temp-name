package com.example.seoulpublicservice.ui.recommendation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seoulpublicservice.databases.ReservationDAO
import com.example.seoulpublicservice.databases.ReservationDatabase
import com.example.seoulpublicservice.databases.ReservationRepositoryImpl
import com.example.seoulpublicservice.databinding.FragmentRecommendationBinding
import com.example.seoulpublicservice.pref.RecommendPrefRepository
import com.example.seoulpublicservice.pref.RowPrefRepositoryImpl
import com.example.seoulpublicservice.seoul.SeoulApiService
import com.example.seoulpublicservice.seoul.SeoulPublicRepositoryImpl
import com.example.seoulpublicservice.usecase.GetAll2000UseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecommendationFragment : Fragment() {

    private var _binding: FragmentRecommendationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RecommendationViewModel
    private lateinit var recommendationAdapter: RecommendationAdapter
    private lateinit var reservationDAO: ReservationDAO
    private lateinit var getAll2000UseCase: GetAll2000UseCase
    private lateinit var recommendPrefRepository: RecommendPrefRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)

        reservationDAO = ReservationDatabase.getDatabase(requireContext()).getReservation()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://openapi.seoul.go.kr:8088")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val seoulApiService = retrofit.create(SeoulApiService::class.java)

        val seoulPublicRepository = SeoulPublicRepositoryImpl(seoulApiService)
        recommendPrefRepository = RecommendPrefRepository(requireContext())

        val rowPrefRepository = RowPrefRepositoryImpl(requireContext())
        getAll2000UseCase =
            GetAll2000UseCase(seoulPublicRepository, recommendPrefRepository, rowPrefRepository)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        initRecyclerView()
    }

    private fun initView() = binding.let { b ->
        b.reArea.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun initViewModel() {
        // ReservationRepository 생성
        val reservationRepository = ReservationRepositoryImpl(reservationDAO)

        // convertToSealedMulti() 호출하여 SealedMulti 리스트 가져오기
//        val sealedMultiList = convertToSealedMulti(reservationRepository)

        // sealedMultiList를 사용하여 작업 수행


        // RecommendationViewModelFactory를 사용하여 ViewModel 초기화
        viewModel = ViewModelProvider(
            this,
            RecommendationViewModelFactory(
                reservationRepository,
                recommendPrefRepository,
                reservationDAO,
                getAll2000UseCase
            )
        ).get(RecommendationViewModel::class.java)

        // fetchData() 호출하여 데이터 가져오기
        viewModel.fetchData()

        // LiveData를 관찰하여 UI 업데이트
        viewModel.regionServices.observe(viewLifecycleOwner) { regionServices ->
            recommendationAdapter.setItems(regionServices)
        }
        viewModel.disabilityServices.observe(viewLifecycleOwner) { disabilityServices ->
            recommendationAdapter.setItems(disabilityServices)
        }
        viewModel.nextWeekServices.observe(viewLifecycleOwner) { nextWeekServices ->
            recommendationAdapter.setItems(nextWeekServices)
        }
    }

    private fun initRecyclerView() {
        recommendationAdapter = RecommendationAdapter()

        // RecyclerView 설정
        binding.reScroll.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.reScroll.adapter = recommendationAdapter

        binding.reArea.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.reArea.adapter = recommendationAdapter

        binding.reDisabled.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.reDisabled.adapter = recommendationAdapter

        binding.reNextWeek.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.reNextWeek.adapter = recommendationAdapter

        // RecyclerView에 아이템 목록 설정
        val items: List<SealedMulti> = mutableListOf() // 아이템 목록
        recommendationAdapter.setItems(items)
        Log.d("RecommendationFragment", "RecyclerView initialized")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}