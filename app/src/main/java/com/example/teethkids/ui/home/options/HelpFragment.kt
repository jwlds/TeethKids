package com.example.teethkids.ui.home.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentHelpBinding
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListHelpOptionAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.functions.FunctionsComponent_MainModule_BindProjectIdFactory


class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.screenName.text = "Ajuda"
        setupActionBar()
        binding.listHelp.setHasFixedSize(true)
        val adapter = ListHelpOptionAdapter(requireContext()) { option ->
            when (option.name) {
                "Atualizar senha" -> Snackbar.make(view, "Senha Atualizada", Snackbar.LENGTH_SHORT)
                    .show()//Chamar snackbar dizendo "senha atualizada"
                "Recuperar senha" -> Snackbar.make(view, "Senha Recuperada", Snackbar.LENGTH_SHORT)
                    .show()//Chamar snackbar dizendo "senha recuperar"
            }

        }

        binding.listHelp.adapter = adapter
    }


    private fun setupActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}