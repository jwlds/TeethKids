package com.example.teethkids.ui.dialog


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.teethkids.dao.AddressDao
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.databinding.DialogContentOptionsAddressBinding
import com.example.teethkids.ui.home.options.MyAddressesFragment
import com.example.teethkids.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment



class OptionAddressDialog(private val addressId: String,
                          private val tittle: String,
) : BottomSheetDialogFragment() {

    private var _binding: DialogContentOptionsAddressBinding? = null
    private val binding get() = _binding!!




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("TEST",addressId)
        binding.titleTextView.text = tittle
        binding.btnDelete.setOnClickListener{
            binding.loading.isVisible = true
            val dao = AddressDao()
            dao.deleteAddress(getIdUser().toString(), addressId,
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
        binding.btnUpdate.setOnClickListener{

        }
        binding.btnCancel.setOnClickListener{
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogContentOptionsAddressBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}