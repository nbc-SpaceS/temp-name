package com.wannabeinseoul.seoulpublicservice.ui.mypage

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databinding.DialogEditProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileDialog : DialogFragment() {

    companion object {
        fun newInstance() = EditProfileDialog()
    }

    private var _binding: DialogEditProfileBinding? = null
    private val binding get() = _binding!!

    private val app by lazy {
        requireActivity().application as SeoulPublicServiceApplication
    }
    private val container by lazy {
        app.container
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEditProfileBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

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

        b.btnEditProfileOkay.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                container.userRepository.updateUserName(container.idPrefRepository.load(), b.etEditProfileName.text.toString())
            }
        }

        b.ivEditProfileImage.setOnClickListener {}
    }

}
