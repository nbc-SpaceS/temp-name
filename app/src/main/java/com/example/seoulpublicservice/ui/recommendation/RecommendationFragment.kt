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
import com.example.seoulpublicservice.pref.PrefRepository
import com.example.seoulpublicservice.pref.RowPrefRepositoryImpl
import com.example.seoulpublicservice.seoul.Row
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
    private lateinit var prefRepositoryImpl: PrefRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)

        val reservationDAO = ReservationDatabase.getDatabase(requireContext()).getReservation()
        val reservationRepository = ReservationRepositoryImpl(reservationDAO)
        val viewModelFactory = RecommendationViewModelFactory(reservationRepository, reservationDAO, getAll2000UseCase)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RecommendationViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        initRecyclerView()

        val retrofit = Retrofit.Builder()
            .baseUrl("6a415a5368646c6431356f75506e71") // 서버의 베이스 URL을 여기에 입력해야 합니다.
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val seoulApiService = retrofit.create(SeoulApiService::class.java)

        val seoulPublicRepository = SeoulPublicRepositoryImpl(seoulApiService)
        val sharedPrefRepository = PrefRepositoryImpl(requireContext())
        val rowPrefRepository = RowPrefRepositoryImpl(requireContext())
        getAll2000UseCase = GetAll2000UseCase(seoulPublicRepository, sharedPrefRepository, rowPrefRepository)

        binding.reFirst.adapter = recommendationAdapter
        binding.reRecommend.adapter = recommendationAdapter
        binding.reRecommend2.adapter = recommendationAdapter
        binding.reRecommend3.adapter = recommendationAdapter

    }

    private fun initView() = binding.let { b ->
        b.reRecommend.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun initViewModel() {
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

        val items: List<ReservationEntity> = mutableListOf() // 아이템 목록을 가져와야 합니다.
        recommendationAdapter.setItems(items)
        Log.d("RecommendationFragment", "RecyclerView initialized")
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
