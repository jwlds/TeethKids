package com.example.teethkids.ui.auth.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentAddressBinding
import com.example.teethkids.repository.ViaCepRepository
import com.example.teethkids.utils.RegistrationDataHolder
import com.example.teethkids.utils.Utils
import com.example.teethkids.utils.Utils.hideKeyboard


class AddressFragment : Fragment(){
    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtZipe.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }

            override fun afterTextChanged(s: Editable?) {
                val cep = binding.edtZipe.unMasked
                if (cep.isNotEmpty()) {
                    if (isValidCep(cep)) {
                        getAddressByCep(cep)
                    } else {
                        binding.zipCodeInputLayout.endIconDrawable = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.baseline_close_24
                        )
                    }
                }
            }
        })

    }

    private fun isValidCep(zipeCode: String): Boolean {
        if (zipeCode.length < 8) return false
        return true
    }
    private fun getAddressByCep(cep: String) {
        val cepRepository = ViaCepRepository()
        cepRepository.getAddressByCep(
            cep,
            onResponse = { address ->
                if (address != null) {
                    hideKeyboard()
                    binding.zipCodeInputLayout.endIconDrawable = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_done_24
                    )
                    binding.edtStreet.setText(address.logradouro)
                    binding.edtNeighbBorhood.setText(address.bairro)
                    binding.edtCity.setText(address.localidade)
                    binding.edtState.setText(address.uf)
                } else {
                    Utils.showToast(
                        requireContext(),
                        "Não foi possível carregar os dados do CEP. Por favor, digite manualmente."
                    )
                }
            },
            onFailure = {
                Log.e("AddAddressDialog", "Failed to get address from ViaCEP")
            }
        )
    }
    fun isValid(): Boolean {
        val zipe = binding.edtZipe.unMasked
        val street = binding.edtStreet.text.toString().trim()
        val number  = binding.edtNumber.text.toString().trim()
        val neighborhood = binding.edtNumber.text.toString().trim()
        val city = binding.edtCity.text.toString().trim()
        val state = binding.edtState.text.toString().trim()

        if (zipe.isEmpty()) {
            binding.edtZipe.error = "Cep não pode ser vazio"
           // binding.edtZipe.
            return false
        }

        if (street.isEmpty()) {
            binding.edtStreet.error = "Rua não pode ser vazio"
            return false
        }

        if (number.isEmpty()) {
            binding.edtNumber.error = "Numero não pode ser vazio"
            return false
        }
        if (neighborhood.isEmpty()) {
            binding.edtNeighbBorhood.error = "Bairro não pode ser vazio"
            return false
        }

        if (city.isEmpty()) {
            binding.edtCity.error = "Cidade não pode ser vazio"
            return false
        }
        if (state.isEmpty()) {
            binding.edtState.error = "Estado não pode ser vazio"
            return false
        }

        RegistrationDataHolder.registrationData.zipcode = zipe
        RegistrationDataHolder.registrationData.street  = street
        RegistrationDataHolder.registrationData.numberStreet = number
        RegistrationDataHolder.registrationData.neighborhood = neighborhood
        RegistrationDataHolder.registrationData.city = city
        RegistrationDataHolder.registrationData.state = state
        return true
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}