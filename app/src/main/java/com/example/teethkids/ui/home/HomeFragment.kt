package com.example.teethkids.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teethkids.R
import com.example.teethkids.database.FirebaseHelper.Companion.getAuth
import com.example.teethkids.databinding.FragmentHomeBinding
import com.example.teethkids.ui.auth.AuthenticateActivity

class HomeFragment : Fragment(),View.OnClickListener{

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnlo.setOnClickListener(this)


    }

    override fun onClick(v: View?) {

        when(v!!.id) {
            R.id.btnlo -> {
                getAuth().signOut()
                val intent = Intent(activity, AuthenticateActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }


}