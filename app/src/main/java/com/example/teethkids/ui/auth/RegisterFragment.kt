package com.example.teethkids.ui.auth

import android.Manifest
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
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.database.FirebaseRoom.Companion.getAuth
import com.example.teethkids.database.FirebaseRoom.Companion.getDatabase
import com.example.teethkids.database.FirebaseRoom.Companion.getIdUser
import com.example.teethkids.databinding.FragmentRegisterBinding
import com.example.teethkids.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream


class RegisterFragment : Fragment(),View.OnClickListener {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var storageReference: StorageReference
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
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener(this)
        binding.imgUser.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnRegister -> {
                try {
                    binding.loading.isVisible = true
                    val email = binding.edtEmail.text.toString().trim()
                    val password = binding.edtPassword.text.toString().trim()
                    val name = binding.edtName.text.toString().trim()
                    val phoneNumber = binding.edtPhoneNumber.text.toString().trim()
                    createAccount(email,password,name,phoneNumber)
                } catch (arg: IllegalArgumentException) {
                    Utils.showToast(requireContext(),"Preecha todos os campos!")
                }

            }
            R.id.imgUser -> checkPermissionGallery()
           R.id.btnBack -> findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

        }
    }


    private fun createAccount(email: String, password: String,name: String,numberPhone: String) {
        getAuth().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    uploadProfile(bitmap).addOnSuccessListener { url ->
                        val currentUser = getIdUser().toString()
                        val userData = hashMapOf(
                            "id" to currentUser,
                            "email" to email,
                            "name" to name,
                            "numberPhone" to numberPhone,
                            "imgUrl" to url.toString()
                        )
                        getDatabase().collection("USERS")
                            .document(currentUser)
                            .set(userData)
                            .addOnSuccessListener {
                                binding.loading.isVisible = false
                                Utils.showToast(requireContext(),"Usuario cadastrado com sucesso")
                                //findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                            }.addOnFailureListener { exception ->
                                val messageError = when (exception) {
                                    is FirebaseAuthUserCollisionException -> "Esse usuario já foi cadastrado"
                                    is FirebaseNetworkException -> "Sem internet"
                                    is FirebaseAuthInvalidCredentialsException -> "Digite um email válido!"
                                    is FirebaseAuthWeakPasswordException -> "Senha com menos de 6 digitos!"
                                    else -> "Usuario não castrato!"
                                }
                                binding.loading.isVisible = false
                                Utils.showToast(requireContext(),messageError)
                            }
                    }
                } else {
                    binding.loading.isVisible = false
                    Utils.showToast(requireContext(),"deu ruim")
                }
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
        ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED


    private fun uploadProfile(img: Bitmap?): Task<Uri> {
        val baos = ByteArrayOutputStream()
        img?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        storageReference =
            FirebaseStorage.getInstance().getReference("USERS/" + "PHOTOS/" + getIdUser().toString())
        val uploadTask = storageReference.putBytes(data)
        return uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            storageReference.downloadUrl
        }
    }


}