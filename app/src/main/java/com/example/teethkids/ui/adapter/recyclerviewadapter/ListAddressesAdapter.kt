package com.example.teethkids.ui.adapter.recyclerviewadapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.databinding.AddressItemBinding
import com.example.teethkids.model.Address
import com.example.teethkids.ui.dialog.OptionAddressDialog
import com.example.teethkids.ui.home.options.MyAddressesFragment
import com.example.teethkids.utils.Utils
import com.example.teethkids.utils.Utils.formatAddress

class ListAddressesAdapter(
    private val context: Context,
): ListAdapter<Address,ListAddressesAdapter.AddressViewHolder>(DIFF_CALLBACK) {





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

    class AddressViewHolder(val binding: AddressItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(addresses: Address) {
            binding.addressItemName.text = "${addresses.street}, ${addresses.number}"
            binding.addressItemDate.text = formatAddress(addresses)
            binding.btnOption.setOnClickListener {
                val dialog = OptionAddressDialog(addresses)
                dialog.show((binding.root.context as AppCompatActivity).supportFragmentManager, "bottomSheetTag")
            }
        }

    }


    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = getItem(position)
        holder.bind(address)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = AddressItemBinding.inflate(inflater, parent, false)

        return AddressViewHolder(binding)
    }

}
