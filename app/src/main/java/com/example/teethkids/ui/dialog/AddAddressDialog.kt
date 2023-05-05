package com.example.teethkids.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.teethkids.dao.AddressDao
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.databinding.DialogContentAddAddressBinding
import com.example.teethkids.model.Address
import com.example.teethkids.model.AddressViaCep
import com.example.teethkids.service.AddressService
import com.example.teethkids.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AddAddressDialog : BottomSheetDialogFragment() {

    private var _binding: DialogContentAddAddressBinding? = null
    private val binding get() = _binding!!

    private lateinit var service: AddressService


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

        binding.btnAdd.setOnClickListener {
            if(isValid()){
                val dao = AddressDao()
                val address = Address(
                    userId = getIdUser().toString(),
                    street = binding.edtStreet.text.toString().trim(),
                    neighborhood = binding.edtNeighbBorhood.text.toString().trim(),
                    zipeCode = binding.edtZipe.unMasked,
                    state = binding.edtState.text.toString().trim(),
                    city = binding.edtCity.text.toString().trim(),
                    number = binding.edtNumber.text.toString().trim()
                )
                dao.addAddress(address,
                    onSuccess = {
                        Utils.showToast(requireContext(), "Endereço adicionado com sucesso!")
                        dismiss()
                    },
                    onFailure = { exception ->
                        Utils.showToast(requireContext(), "Erro ao adicionar endereço: ${exception.message}")
                    }
                )
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogContentAddAddressBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    private fun getAddressByCep(cep: String) {
        val call = service.getAddress(cep)
        call.enqueue(object : Callback<AddressViaCep> {
            override fun onResponse(call: Call<AddressViaCep>, response: Response<AddressViaCep>) {
                binding.loading.isVisible = true
                if (response.isSuccessful) {
                    val address = response.body()
                    address?.let {
                        binding.loading.isVisible = false
                        binding.edtStreet.setText(it.logradouro)
                        binding.edtNeighbBorhood.setText(it.bairro)
                        binding.edtCity.setText(it.localidade)
                        binding.edtState.setText(it.uf)
                    }
                } else {
                    binding.loading.isVisible = false
                    Utils.showToast(requireContext(),"Não foi possível carregar os dados do CEP. Por favor, digite manualmente.")
                }
            }

            override fun onFailure(call: Call<AddressViaCep>, t: Throwable) {

            }
        })
    }

    private fun isValid(): Boolean {
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
        return true
    }

}