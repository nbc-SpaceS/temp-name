package com.example.seoulpublicservice.ui.recommendation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.seoulpublicservice.databinding.FragmentRecommendationBinding
import com.example.seoulpublicservice.detail.DetailFragment
import com.example.seoulpublicservice.ui.notifications.NotificationsViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecommendationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecommendationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentRecommendationBinding? = null
    val binding get() = _binding!!
    private val viewModel: NotificationsViewModel by viewModels { NotificationsViewModel.factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnTemp.setOnClickListener{
            val dialog = DetailFragment.newInstance("S240104091254073361")
            dialog.show(requireActivity().supportFragmentManager, "DetailFragment")
            Log.i("This is NotifiFragment","DF Activate? : $dialog")
            viewModel.setRandomOne()
        }
        viewModel.text.observe(viewLifecycleOwner) {
            binding.textRecommend.text = it
        }
        viewModel.isBtnEnabled.observe(viewLifecycleOwner) {
            binding.btnTemp.isEnabled = it
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}