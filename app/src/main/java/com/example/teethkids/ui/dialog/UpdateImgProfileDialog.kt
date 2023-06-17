package com.example.teethkids.ui.dialog

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.teethkids.R
import com.example.teethkids.dao.UserDao
import com.example.teethkids.Helper.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.databinding.DialogContentOptionsUpdateImgProfileBinding
import com.example.teethkids.utils.RegistrationDataHolder
import com.example.teethkids.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.Random

class UpdateImgProfileDialog(private val name: String?) : BottomSheetDialogFragment(),
    OnClickListener {

    private var _binding: DialogContentOptionsUpdateImgProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog: AlertDialog
    private var bitmap: Bitmap? = null

    companion object {
        private const val PERMISSION_GALLERY = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val PERMISSION_CAMERA = Manifest.permission.CAMERA
    }

    private val requestCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permission ->
            when {
                permission -> {
                    openCamera()
                }

                else -> {
                    showDialogPermissionCamera()
                }
            }
        }

    private val resultCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            binding.loading.isVisible = true
            if (result.resultCode == Activity.RESULT_OK) {
                bitmap = result.data?.extras?.get("data") as Bitmap?
                Utils.updateProfileImage(bitmap, getIdUser().toString())
                    .addOnSuccessListener { url ->
                        binding.loading.isVisible = false
                        val dao = UserDao(requireContext())
                        dao.updateUrlProfileImage(url.toString(), onSuccess = {
                            dismiss()
                        })
                    }
            }
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

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnGallery -> checkPermissionGallery()
            R.id.btnCamera -> checkPermissionCamera()
            R.id.btnDeletePhoto -> createInitialsImage()
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
                binding.loading.isVisible = true
                Utils.updateProfileImage(bitmap, getIdUser().toString())
                    .addOnSuccessListener { url ->
                        binding.loading.isVisible = false
                        val dao = UserDao(requireContext())
                        dao.updateUrlProfileImage(url.toString(), onSuccess = {
                            dismiss()
                        })
                    }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCamera.setOnClickListener(this)
        binding.btnGallery.setOnClickListener(this)
        binding.btnDeletePhoto.setOnClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DialogContentOptionsUpdateImgProfileBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }


    private fun checkPermissionCamera() {
        val permissionCamera = checkPermission(PERMISSION_CAMERA)

        when {
            permissionCamera -> {
                openCamera()
            }

            shouldShowRequestPermissionRationale(PERMISSION_CAMERA) -> showDialogPermissionCamera()

            else -> requestCamera.launch(PERMISSION_CAMERA)
        }
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

    private fun showDialogPermissionCamera() {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Atenção")
            .setMessage("Precisamos do acesso à câmera do seu dispositivo, deseja permitir agora?")
            .setPositiveButton("Sim") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Não") { _, _ ->
                dialog.dismiss()
            }
        dialog = builder.create()
        dialog.show()
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

    private fun createInitialsImage() {
        val initials = Utils.getInitials(name!!)
        val width = 120
        val height = 120

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            val random = Random()
            color = Color.rgb(random.nextInt(255), random.nextInt(256), random.nextInt(256))
            style = Paint.Style.FILL
        }

        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2).toFloat(),
            paint
        )

        paint.apply {
            color = Color.WHITE
            textSize = 60f
            textAlign = Paint.Align.CENTER
        }

        val xPos = canvas.width / 2
        val yPos = (canvas.height / 2) - ((paint.descent() + paint.ascent()) / 2)

        initials?.let { canvas.drawText(it, xPos.toFloat(), yPos, paint) }

        // Enviar a imagem para o Firebase Storage
        val storageRef = Firebase.storage.reference
        val profileImagesRef = storageRef.child("profile_images")
        val imageRef = profileImagesRef.child("${getIdUser()}.jpg")

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageData = baos.toByteArray()

        val uploadTask = imageRef.putBytes(imageData)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                binding.loading.isVisible = true
                setProfileImage(downloadUri.toString())
            } else {
                // Tratar falha ao enviar a imagem para o Firebase Storage
            }
        }
    }

    private fun setProfileImage(url: String) {

        val user = FirebaseAuth.getInstance().currentUser
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(Uri.parse(url))
            .build()
        user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
            if (updateTask.isSuccessful) {

                val dao = UserDao(requireContext())
                binding.loading.isVisible = false
                dao.updateUrlProfileImage(url) {
                    dismiss()
                }
            } else {
                // Tratar falha ao atualizar a imagem do perfil no Firebase Auth
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultCamera.launch(intent)
    }

    private fun checkPermission(permission: String) =
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    fun isValid(): Boolean {
        if (bitmap == null) {
            return true
        }
        RegistrationDataHolder.registrationData.photo = bitmap
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}