package com.example.teethkids.ui.dialog


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.teethkids.dao.AddressDao
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.databinding.DialogContentOptionsAddressBinding
import com.example.teethkids.model.Address
import com.example.teethkids.ui.home.options.MyAddressesFragment
import com.example.teethkids.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class OptionAddressDialog(
    private val address: Address,
) : BottomSheetDialogFragment() {

    private var _binding: DialogContentOptionsAddressBinding? = null
    private val binding get() = _binding!!




    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleTextView.text = "${address.street}, ${address.number}"
        binding.btnDelete.setOnClickListener{
            if(address.primary) {
                Utils.showToast(requireContext(),"Você precisa ter pelo menos um endereço cadastrado.")
            }
            else {
                binding.loading.isVisible = true
                val dao = AddressDao()
                dao.deleteAddress(getIdUser().toString(), address.addressId,
                    onSuccess = {
                        binding.loading.isVisible = false
                        dismiss()
                    },
                    onFailure = { errorMessage ->
                        binding.loading.isVisible = false
                        Log.d("Erro22",errorMessage.toString())
                        Utils.showToast(requireContext(),errorMessage.toString())
                        //dismiss()
                    }
                )
            }
        }
        binding.btnUpdate.setOnClickListener{
            showDialogUpdate()
        }
        binding.btnCancel.setOnClickListener{
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogContentOptionsAddressBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    private fun showDialogUpdate() {
        val dialog = UpdateAddressDialog(address)
        dialog.show(requireActivity().supportFragmentManager, "FullScreenDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}