package com.example.teethkids.ui.adapter.recyclerviewadapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.databinding.EmergencyItemBinding
import com.example.teethkids.model.Emergency
import com.example.teethkids.model.Option
import com.example.teethkids.utils.Utils

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
            val locationEmergency = emergencies.location
//            if(locationEmergency != null){
//                binding.locationTextView.text = Utils.calculateDistance(
//                    -22.903449,
//                    -47.063588,
//                    locationEmergency[0],
//                    locationEmergency[1]
//                    ).toString()
//            }
            binding.nameTextView.text = emergencies.name
            binding.phoneTextView.text = emergencies.phoneNumber
            binding.tvStatus.text = emergencies.status
            binding.dateTimeTextView.text = Utils.formatTimestamp(emergencies.createdAt!!)
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