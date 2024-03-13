package com.wannabeinseoul.seoulpublicservice.ui.mypage

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import coil.load
import com.wannabeinseoul.seoulpublicservice.SeoulPublicServiceApplication
import com.wannabeinseoul.seoulpublicservice.databinding.DialogEditProfileBinding
import com.wannabeinseoul.seoulpublicservice.util.toastShort
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileDialog : DialogFragment() {

    companion object {
        fun newInstance() = EditProfileDialog()
    }

    private var _binding: DialogEditProfileBinding? = null
    private val binding get() = _binding!!

    private val app by lazy { requireActivity().application as SeoulPublicServiceApplication }
    private val container by lazy { app.container }
    private val id by lazy { container.idPrefRepository.load() }

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
                container.userRepository.updateUserName(id, b.etEditProfileName.text.toString())
            }

            // TODO: 종료될 때 닉네임 넘겨주기, 프사 uri 넘겨주기

            dismiss()
        }

        b.ivEditProfileImage.setOnClickListener {
            openGalleryWithPermissionCheck()
        }
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) openGalleryPermitted()
            else Log.w("jj-에딧프로필", "콜백:requestPermissionLauncher not granted")
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                Log.e("jj-에딧프로필", "콜백:pickImageLauncher resultCode != RESULT_OK, result: $result")
                return@registerForActivityResult
            }
            val uri = result.data?.data ?: return@registerForActivityResult Unit.apply {
                Log.e("jj-에딧프로필", "콜백:pickImageLauncher result.data: ${result.data}")
            }
            Log.d("jj-에딧프로필", "콜백:pickImageLauncher uri: $uri")

            binding.ivEditProfileImage.load(uri)

            // TODO: 확인 눌렀을 때로 옮겨야 함
            CoroutineScope(Dispatchers.IO).launch {
                container.userProfileRepository.uploadProfileImage(id, uri)
            }
        }

    private fun openGalleryPermitted() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun openGalleryWithPermissionCheck() {
        // TODO: 한번 권한 거부하면 다시 요청이 불가능한 문제. 버튼이 무반응이 된다. 해결 불가능한가..? 일단 토스트로 반응.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = android.Manifest.permission.READ_MEDIA_IMAGES
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                toastShort(requireContext(), "미디어 이미지 권한이 필요합니다")
                requestPermissionLauncher.launch(permission)
            } else openGalleryPermitted()
        } else {
            val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                toastShort(requireContext(), "외부 저장소 권한이 필요합니다")
                requestPermissionLauncher.launch(permission)
            } else openGalleryPermitted()
        }
    }

//    /* ActivityResultContracts.GetContent() 로 하는 것도 있더라 */
//    private val getContentLauncher =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            Log.d("jj-에딧프로필", "콜백:getContentLauncher, uri: $uri")
//        }
//
//    private fun openGallery() {
//        getContentLauncher.launch("image/*")
//    }

}
