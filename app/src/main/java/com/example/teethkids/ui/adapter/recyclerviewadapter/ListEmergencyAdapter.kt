package com.example.teethkids.ui.adapter.recyclerviewadapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.dao.EmergencyDao
import com.example.teethkids.databinding.EmergencyItemBinding
import com.example.teethkids.model.Emergency
import com.example.teethkids.utils.AddressPrimaryId
import com.example.teethkids.utils.Utils
import com.google.firebase.firestore.GeoPoint

class ListEmergencyAdapter(
private val context: Context,
private val onEmergencyClicked: (Emergency) -> Unit
): ListAdapter<Emergency, ListEmergencyAdapter.EmergencyViewHolder>(DIFF_CALLBACK) {





    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Emergency>() {
            override fun areItemsTheSame(oldItem: Emergency, newItem: Emergency): Boolean {
                return oldItem.rescuerUid == newItem.rescuerUid
            }

            override fun areContentsTheSame(oldItem: Emergency, newItem: Emergency): Boolean {
                return oldItem == newItem
            }
        }
    }

    class EmergencyViewHolder(val binding: EmergencyItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(emergencies: Emergency,onEmergencyClicked: (Emergency) -> Unit) {

            if(emergencies.location != null && AddressPrimaryId.addressGeoPoint != null){
                binding.locationTextView.text = Utils.calculateDistance(
                    GeoPoint(emergencies.location[0],emergencies.location[1]),
                    AddressPrimaryId.addressGeoPoint!!
                )
            }




            binding.nameTextView.text = emergencies.name
            binding.phoneTextView.text = emergencies.phoneNumber
            binding.tvStatus.text = Utils.translateStatus(emergencies.status)
            binding.dateTimeTextView.text = Utils.formatTimestamp(emergencies.createdAt!!)
            binding.btnDetails.setOnClickListener {
                onEmergencyClicked(emergencies)
            }


//            binding.btnOption.setOnClickListener {
//                Utils.showToast(binding.root.context,addresses.addressId)
//                val dialog = OptionAddressDialog(addresses.addressId,"${addresses.street}, ${addresses.number}" )
//                dialog.show((binding.root.context as AppCompatActivity).supportFragmentManager, "bottomSheetTag")
//            }
        }

    }


    override fun onBindViewHolder(holder: EmergencyViewHolder, position: Int) {
        val emergencies = getItem(position)
        holder.bind(emergencies, onEmergencyClicked)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = EmergencyItemBinding.inflate(inflater, parent, false)

        return EmergencyViewHolder(binding)
    }

}