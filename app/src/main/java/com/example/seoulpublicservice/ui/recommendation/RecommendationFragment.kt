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
import com.example.seoulpublicservice.databases.ReservationEntity
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
        b.reRecommend.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun initViewModel() {
        val reservationRepository = ReservationRepositoryImpl(reservationDAO)
        viewModel = ViewModelProvider(
            this,
            RecommendationViewModelFactory(
                reservationRepository,
                recommendPrefRepository,
                reservationDAO,
                getAll2000UseCase
            )
        ).get(RecommendationViewModel::class.java)

        viewModel.fetchData()
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
        binding.reFirst.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.reFirst.adapter = recommendationAdapter

        binding.reRecommend.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.reRecommend.adapter = recommendationAdapter

        binding.reRecommend2.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.reRecommend2.adapter = recommendationAdapter

        binding.reRecommend3.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.reRecommend3.adapter = recommendationAdapter

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