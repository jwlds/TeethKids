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
import com.example.teethkids.dao.EmergencyDao
import com.example.teethkids.databinding.EmergencyItemBinding
import com.example.teethkids.databinding.MyEmergencyItemBinding
import com.example.teethkids.model.Emergency
import com.example.teethkids.utils.AddressPrimaryId
import com.example.teethkids.utils.Utils
import com.google.firebase.firestore.GeoPoint
import java.util.logging.Handler

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
            binding.btnDetails.isEnabled = emergencies.status != "waiting"
            binding.btnCall.isVisible = emergencies.status != "onGoing"
            binding.myEmergencyTimeRemaing.isVisible = emergencies.status != "onGoing"
            binding.clockIcon.isVisible = emergencies.status != "onGoing"
            binding.myEmergencyPhone.isVisible = true
            binding.myEmergencyName.text = emergencies.name
            binding.myEmergencyPhone.text = emergencies.phoneNumber
            binding.myEmergencyStatus.text = emergencies.status
            binding.myEmergencyDate.text = Utils.formatTimestamp(emergencies.createdAt!!)
            binding.btnDetails.setOnClickListener {
                onEmergencyClicked(emergencies)
            }
            binding.btnCall.setOnClickListener{
                val dao = EmergencyDao()
                dao.updateStatusEmergency(emergencies.rescuerUid!!,"onGoing", onSuccess = {}, onFailure = {})
                dao.updateStatusMyEmergency(emergencies.rescuerUid,"onGoing", onSuccess = {}, onFailure = {})
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${emergencies.phoneNumber}")
                ContextCompat.startActivity(binding.root.context, intent, null)
            }

        }

    }

    private fun startTimer(holder: MyEmergencyViewHolder, emergency: Emergency) {
        val handler = android.os.Handler()
        val delay = 1000L

        val runnable = object : Runnable {
            override fun run() {
                if (emergency.timer > 0) {
                    emergency.timer -= 1
                    holder.binding.myEmergencyTimeRemaing.text = emergency.timer.toString()
                    handler.postDelayed(this, delay)
                } else {
                    val dao = EmergencyDao()
                    dao.updateStatusMyEmergency(emergencyId = emergency.rescuerUid.toString(),"expired", onSuccess = {}, onFailure = {} )
                }
            }
        }

        handler.postDelayed(runnable, delay)
    }


    override fun onBindViewHolder(holder: MyEmergencyViewHolder, position: Int) {
        val emergencies = getItem(position)
        holder.bind(emergencies, onEmergencyClicked)
        if(emergencies.status == "waiting") startTimer(holder, emergencies)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyEmergencyViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = MyEmergencyItemBinding.inflate(inflater, parent, false)

        return MyEmergencyViewHolder(binding)
    }

}