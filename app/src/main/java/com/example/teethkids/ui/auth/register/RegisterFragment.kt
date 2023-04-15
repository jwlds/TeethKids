package com.example.teethkids.ui.auth.register

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
import androidx.viewpager2.widget.ViewPager2
import com.example.teethkids.R
import com.example.teethkids.database.FirebaseRoom.Companion.getAuth
import com.example.teethkids.database.FirebaseRoom.Companion.getDatabase
import com.example.teethkids.database.FirebaseRoom.Companion.getIdUser
import com.example.teethkids.databinding.FragmentRegisterBinding
import com.example.teethkids.ui.adapter.viewPagerAdapter.StageRegisterAdapter
import com.example.teethkids.utils.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream


class RegisterFragment : Fragment(){
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: StageRegisterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicialize o ViewPager2 e o adapter
        viewPager = binding.SlideViewPager
        adapter = StageRegisterAdapter(this)
        viewPager.adapter = adapter

        // Adicione os fragmentos para cada etapa do registro ao adapter
        adapter.addFragment(EmailPasswordFragment())
        adapter.addFragment(ProfileFragment())
        adapter.addFragment(AndressFragment())
        adapter.addFragment(EducationFragment())
        adapter.addFragment(PhotoFragment())

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Ocultar botão "Voltar" na primeira etapa
                if (position == 0) {
                    binding.btnBack.visibility = View.GONE
                } else {
                    binding.btnBack.visibility = View.VISIBLE
                }
                // Alterar o texto do botão "Avançar" na última etapa
                if (position == adapter.itemCount - 1) {
                    binding.btnNext.setText(R.string.finish)
                } else {
                    binding.btnNext.setText(R.string.next)
                }
            }
        })
        binding.btnBack.setOnClickListener {
            val currentItem = viewPager.currentItem
            viewPager.setCurrentItem(currentItem - 1, true)
        }


        binding.btnNext.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem == adapter.itemCount - 1) {
                // Finalizar registro
                //  finishRegistration()
            } else {
                val currentFragment = adapter.fragments[currentItem]
                when (val currentFragment = adapter.fragments[currentItem]) {
                    is EmailPasswordFragment -> if (currentFragment.isValid())
                        viewPager.setCurrentItem(currentItem + 1, true)
                    is ProfileFragment -> if (currentFragment.isValid())
                        viewPager.setCurrentItem(currentItem + 1, true)
                    is AndressFragment -> if (currentFragment.isValid())
                        viewPager.setCurrentItem(currentItem + 1, true)
                    is EducationFragment -> if (currentFragment.isValid())
                        viewPager.setCurrentItem(currentItem + 1, true)
                    is PhotoFragment -> if (currentFragment.isValid())
                        viewPager.setCurrentItem(currentItem + 1, true)
                    else -> Utils.showToast(requireContext(),"Preecha todos os campos")

                }
            }
        }


    }


}