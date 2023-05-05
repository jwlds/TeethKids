package com.example.teethkids.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.teethkids.R
import com.example.teethkids.dao.AuthenticationDAO
import com.example.teethkids.databinding.FragmentRegisterBinding
import com.example.teethkids.ui.adapter.viewPagerAdapter.StageRegisterAdapter
import com.example.teethkids.utils.RegistrationDataHolder
import com.example.teethkids.utils.Utils


class RegisterFragment : Fragment(),View.OnClickListener{

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


        viewPager = binding.SlideViewPager
        viewPager.isUserInputEnabled = false;
        adapter = StageRegisterAdapter(this)
        viewPager.adapter = adapter


        adapter.addFragment(EmailPasswordFragment())
        adapter.addFragment(ProfileFragment())
        adapter.addFragment(AddressFragment())
        adapter.addFragment(EducationFragment())
        adapter.addFragment(PhotoFragment())

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == adapter.itemCount - 1) {
                    binding.btnNext.setText(R.string.finish)
                } else {
                    binding.btnNext.setText(R.string.next)
                }
            }
        })

        binding.btnNext.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnNext -> {
                val currentItem = viewPager.currentItem
                when (val currentFragment = adapter.fragments[currentItem]) {
                    is EmailPasswordFragment -> if (currentFragment.isValid())
                        viewPager.setCurrentItem(currentItem + 1, true)
                    is ProfileFragment -> if (currentFragment.isValid())
                        viewPager.setCurrentItem(currentItem + 1, true)
                    is AddressFragment -> if (currentFragment.isValid())
                        viewPager.setCurrentItem(currentItem + 1, true)
                    is EducationFragment -> if (currentFragment.isValid())
                        viewPager.setCurrentItem(currentItem + 1, true)
                    is PhotoFragment -> if (currentFragment.isValid())
                    {
                        binding.loading.isVisible = true
                        val registrationData = RegistrationDataHolder.registrationData
                        val auth = AuthenticationDAO()
                        auth.registerAccount(
                            registrationData,
                            onSuccess = {
                                binding.loading.isVisible = false
                                Utils.showSnackbar(requireView(),"Cadastro realizado com sucesso!")
                                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                            },
                            onFailure = { exception ->
                                binding.loading.isVisible = false
                                Utils.showSnackbar(requireView(),exception)
                            }
                        )
                    }
                    else -> Utils.showToast(requireContext(),"Preecha todos os campos")

                }
            }
            R.id.btnBack -> {
                val currentItem = viewPager.currentItem
                if(currentItem == 0){
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                else {
                    viewPager.setCurrentItem(currentItem - 1, true)
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
