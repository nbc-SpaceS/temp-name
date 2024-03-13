package com.wannabeinseoul.seoulpublicservice.ui.dialog.complaint

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.wannabeinseoul.seoulpublicservice.ui.main.MainViewModel
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.databinding.FragmentComplaintBinding

class ComplaintFragment : DialogFragment() {

    companion object {
        fun newInstance() = ComplaintFragment()
    }

    private var _binding: FragmentComplaintBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ComplaintViewModel by viewModels { ComplaintViewModel.factory }
    private val mainViewModel: MainViewModel by activityViewModels()

    private val complaintUserInfo by lazy {
        mainViewModel.complaintUserInfo
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComplaintBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
    }

    private fun initView() = with(binding) {
        tvComplaintDescription.text = requireContext().getString(R.string.complaint_description, complaintUserInfo.complaintName)

        btnComplaintCancel.setOnClickListener {
            dismiss()
        }

        btnComplaintOkay.setOnClickListener {
            viewModel.addComplaint(complaintUserInfo)
        }
    }

    private fun initViewModel() = with(viewModel) {
        resultString.observe(viewLifecycleOwner) {
            if (it == "신고했습니다.") {
                Toast.makeText(requireContext(), "${complaintUserInfo.complaintName}을(를) $it", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }

            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        mainViewModel.setReviewListState(true)
        super.onDismiss(dialog)
    }
}