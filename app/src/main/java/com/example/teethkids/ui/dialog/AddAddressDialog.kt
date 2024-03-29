package com.example.teethkids.ui.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.teethkids.R
import com.example.teethkids.dao.AddressDao
import com.example.teethkids.Helper.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.databinding.DialogContentAddAddressBinding
import com.example.teethkids.model.Address
import com.example.teethkids.repository.ViaCepRepository
import com.example.teethkids.utils.Utils
import com.example.teethkids.utils.Utils.geocodeAddress
import com.example.teethkids.utils.Utils.getFullAddress
import com.example.teethkids.utils.Utils.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddAddressDialog() : BottomSheetDialogFragment() {

    private var _binding: DialogContentAddAddressBinding? = null
    private val binding get() = _binding!!


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



        binding.btnAdd.setOnClickListener {
            if(isValid()){
                binding.btnAdd.startAnimation();
                val dao = AddressDao(requireContext())
                val geoPointer = geocodeAddress(
                    getFullAddress(street = binding.edtStreet.text.toString().trim(),
                        number = binding.edtNumber.text.toString().trim(),
                        neighborhood = binding.edtNeighbBorhood.text.toString().trim(),
                        city = binding.edtCity.text.toString().trim(),
                        state = binding.edtState.text.toString().trim(),
                        zipCode = binding.edtZipe.unMasked
                    ),
                )

               Log.d("111",geoPointer.toString())
                val address = Address(
                    userId = getIdUser().toString(),
                    street = binding.edtStreet.text.toString().trim(),
                    neighborhood = binding.edtNeighbBorhood.text.toString().trim(),
                    zipeCode = binding.edtZipe.unMasked,
                    state = binding.edtState.text.toString().trim(),
                    city = binding.edtCity.text.toString().trim(),
                    number = binding.edtNumber.text.toString().trim(),
                    primary = false,
                    lat = geoPointer!!.first,
                    lng = geoPointer.second
                )
                dao.addAddress(address,
                    onSuccess = {
                        binding.btnAdd.revertAnimation();
                        Utils.showToast(requireContext(), "Endereço adicionado com sucesso!")
                        dismiss()
                    },
                    onFailure = { exception ->
                        binding.btnAdd.revertAnimation();
                        Log.d("333",exception.toString())
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


    private fun isValidCep(zipeCode: String): Boolean {
        if (zipeCode.length < 8) return false
        return true
    }

    private fun getAddressByCep(cep: String) {
        val viaCepRepository = ViaCepRepository()
        viaCepRepository.getAddressByCep(
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


    private fun isValid(): Boolean {

        val street = binding.edtStreet.text.toString().trim()
        val number  = binding.edtNumber.text.toString().trim()
        val neighborhood = binding.edtNumber.text.toString().trim()
        val city = binding.edtCity.text.toString().trim()
        val state = binding.edtState.text.toString().trim()



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

    override fun onDestroy() {
        super.onDestroy()
        binding.btnAdd.dispose()
        _binding = null
    }

}