package com.example.seoulpublicservice.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.seoulpublicservice.databinding.DialogEditProfileBinding

class EditProfileDialog : DialogFragment() {

    companion object {
        fun newInstance() = EditProfileDialog()
    }

    private var _binding: DialogEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() = binding.let { b ->
        b.btnEditProfileCancel.setOnClickListener { dismiss() }

        b.btnEditProfileOkay.setOnClickListener {}

        b.ivEditProfileImage.setOnClickListener {}
    }

}
