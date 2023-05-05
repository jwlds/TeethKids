package com.example.teethkids.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.teethkids.databinding.FragmentAddressBinding
import com.example.teethkids.model.Address
import com.example.teethkids.model.AddressViaCep
import com.example.teethkids.service.AddressService
import com.example.teethkids.utils.RegistrationDataHolder
import com.example.teethkids.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AddressFragment : Fragment(){
    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var service: AddressService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(AddressService::class.java)

        binding.edtZipe.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val cep = binding.edtZipe.text.toString().trim()
                if (cep.isNotEmpty()) {
                    getAddressByCep(cep)
                }
            }
        }
    }
    private fun getAddressByCep(cep: String) {
        val call = service.getAddress(cep)
        call.enqueue(object : Callback<AddressViaCep> {
            override fun onResponse(call: Call<AddressViaCep>, response: Response<AddressViaCep>) {
                if (response.isSuccessful) {
                    val address = response.body()
                    address?.let {
                        binding.edtStreet.setText(it.logradouro)
                        binding.edtNeighbBorhood.setText(it.bairro)
                        binding.edtCity.setText(it.localidade)
                        binding.edtState.setText(it.uf)
                    }
                } else {
                    Utils.showToast(requireContext(),"Não foi possível carregar os dados do CEP. Por favor, digite manualmente.")
                }
            }

            override fun onFailure(call: Call<AddressViaCep>, t: Throwable) {

            }
        })
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