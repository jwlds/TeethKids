package com.example.teethkids.ui.auth.register

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.dao.AuthenticationDAO
import com.example.teethkids.databinding.FragmentPhotoBinding
import com.example.teethkids.utils.RegistrationDataHolder
import com.example.teethkids.utils.Utils
import com.google.firebase.storage.StorageReference


class PhotoFragment : Fragment(),View.OnClickListener{

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: AlertDialog

    //Img
    private var bitmap: Bitmap? = null

    companion object {
        private val PERMISSION_GALLERY = Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private val requestGallery =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            when {
                permission -> {
                    resultGallery.launch(
                        Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                    )
                }
                else -> {
                    showDialogPermisson()
                }
            }

        }

    private val resultGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data?.data != null) {
                bitmap = if (Build.VERSION.SDK_INT < 20) {
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().baseContext.contentResolver,
                        result.data?.data
                    )
                } else {
                    val source = ImageDecoder.createSource(
                        requireContext().contentResolver,
                        result.data?.data!!
                    )
                    ImageDecoder.decodeBitmap(source)
                }
                binding.imgUser.setImageBitmap(bitmap)
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGallery.setOnClickListener(this)
    }

    private fun checkPermissionGallery() {
        val permissionGallery = checkPermission(PERMISSION_GALLERY)

        when {
            permissionGallery -> {
                resultGallery.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            }
            shouldShowRequestPermissionRationale(PERMISSION_GALLERY) -> showDialogPermisson()

            else -> requestGallery.launch(PERMISSION_GALLERY)
        }
    }

    private fun showDialogPermisson() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Atenção")
            .setMessage("Precisamos do acesso a galeria do seu dispositivo, deseja permitir agora ?")
            .setPositiveButton("Sim") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", requireActivity().packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            }.setNegativeButton("Não") { _, _ ->
                dialog.dismiss()
            }
        dialog = builder.create()
        dialog.show()

    }

    private fun checkPermission(permission: String) =
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    fun isValid(): Boolean {
        if (bitmap === null) {
            Utils.showToast(requireContext(), "Precisamos de uma foto sua")
            return false
        }
        RegistrationDataHolder.registrationData.photo = bitmap
        return true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnGallery -> checkPermissionGallery()
        }
    }
}


