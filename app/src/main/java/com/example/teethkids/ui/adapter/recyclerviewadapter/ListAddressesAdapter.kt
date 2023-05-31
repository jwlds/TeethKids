package com.example.teethkids.ui.adapter.recyclerviewadapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.dao.AddressDao
import com.example.teethkids.databinding.AddressItemBinding
import com.example.teethkids.model.Address
import com.example.teethkids.ui.dialog.ConfirmationMainAddressDialog
import com.example.teethkids.ui.dialog.OptionAddressDialog
import com.example.teethkids.utils.AddressPrimaryId
import com.example.teethkids.utils.Utils
import com.example.teethkids.utils.Utils.formatAddress
class ListAddressesAdapter(
    private val context: Context,
) : ListAdapter<Address, ListAddressesAdapter.AddressViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Address>() {
            override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
                // Check if the items have the same ID or unique identifier
                return oldItem.addressId == newItem.addressId
            }

            override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
                // Check if the contents of the items are the same, assuming the addressId is unique
                return oldItem == newItem
            }
        }

    }

    class AddressViewHolder(val binding: AddressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(addresses: Address) {
            binding.cardAddress.isChecked = addresses.primary
            binding.addressItemName.text = "${addresses.street}, ${addresses.number}"
            binding.addressItemDate.text = formatAddress(addresses)
            binding.btnOption.setOnClickListener {
                val dialog = OptionAddressDialog(addresses)
                dialog.show(
                    (binding.root.context as AppCompatActivity).supportFragmentManager,
                    "bottomSheetTag"
                )
            }
            binding.cardAddress.setOnLongClickListener{
                if(addresses.primary) {
                   Utils.showSnackbar(view = binding.root.rootView,"Esse endereço é o principal.")
                }
                else {
                    val confirmationDialog = ConfirmationMainAddressDialog(binding.root.context,addresses.addressId,AddressPrimaryId.addressPrimaryId)
                    confirmationDialog.show()
                }
                true

            }
        }
    }









    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = getItem(position)
        holder.bind(address)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = AddressItemBinding.inflate(inflater, parent, false)

        return AddressViewHolder(binding)
    }

}
