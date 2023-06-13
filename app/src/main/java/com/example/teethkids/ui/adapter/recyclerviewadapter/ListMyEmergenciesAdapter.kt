package com.example.teethkids.ui.adapter.recyclerviewadapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.databinding.EmergencyItemBinding
import com.example.teethkids.databinding.MyEmergencyItemBinding
import com.example.teethkids.model.Emergency
import com.example.teethkids.utils.AddressPrimaryId
import com.example.teethkids.utils.Utils
import com.google.firebase.firestore.GeoPoint

class ListMyEmergenciesAdapter(
    private val context: Context,
    private val onEmergencyClicked: (Emergency) -> Unit
): ListAdapter<Emergency, ListMyEmergenciesAdapter.MyEmergencyViewHolder>(DIFF_CALLBACK) {





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

    class MyEmergencyViewHolder(val binding: MyEmergencyItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(emergencies: Emergency, onEmergencyClicked: (Emergency) -> Unit) {
            if(emergencies.location != null && AddressPrimaryId.addressGeoPoint != null){
                binding.myEmergencyDistance.text = Utils.calculateDistance(
                    emergencies.location,
                    AddressPrimaryId.addressGeoPoint!!
                )
            }
            if(emergencies.status == "waiting") binding.btnDetails.isEnabled = false
            else binding.btnDetails.isEnabled = true
            binding.myEmergencyPhone.isVisible = true
            binding.myEmergencyName.text = emergencies.name
            binding.myEmergencyPhone.text = emergencies.phoneNumber
            binding.myEmergencyStatus.text = emergencies.status
            binding.myEmergencyDate.text = Utils.formatTimestamp(emergencies.createdAt!!)
            binding.btnDetails.setOnClickListener {
                onEmergencyClicked(emergencies)
            }
            binding.btnCall.setOnClickListener{
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${emergencies.phoneNumber}")
                ContextCompat.startActivity(binding.root.context, intent, null)
            }

//            binding.btnOption.setOnClickListener {
//                Utils.showToast(binding.root.context,addresses.addressId)
//                val dialog = OptionAddressDialog(addresses.addressId,"${addresses.street}, ${addresses.number}" )
//                dialog.show((binding.root.context as AppCompatActivity).supportFragmentManager, "bottomSheetTag")
//            }
        }

    }


    override fun onBindViewHolder(holder: MyEmergencyViewHolder, position: Int) {
        val emergencies = getItem(position)
        holder.bind(emergencies, onEmergencyClicked)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyEmergencyViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = MyEmergencyItemBinding.inflate(inflater, parent, false)

        return MyEmergencyViewHolder(binding)
    }

}