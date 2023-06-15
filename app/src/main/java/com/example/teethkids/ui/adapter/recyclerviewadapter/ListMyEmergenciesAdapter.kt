package com.example.teethkids.ui.adapter.recyclerviewadapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.dao.EmergencyDao
import com.example.teethkids.databinding.MyEmergencyItemBinding
import com.example.teethkids.model.Emergency
import com.example.teethkids.utils.AddressPrimaryId
import com.example.teethkids.utils.Utils
import com.google.firebase.firestore.GeoPoint

class ListMyEmergenciesAdapter(
    private val context: Context,
    private val onEmergencyClicked: (Emergency) -> Unit
) : ListAdapter<Emergency, ListMyEmergenciesAdapter.MyEmergencyViewHolder>(DIFF_CALLBACK) {

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

    class MyEmergencyViewHolder(val binding: MyEmergencyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var countDownTimer: CountDownTimer? = null

        fun bind(emergencies: Emergency, onEmergencyClicked: (Emergency) -> Unit) {
            if (emergencies.location != null && AddressPrimaryId.addressGeoPoint != null) {
                binding.myEmergencyDistance.text = Utils.calculateDistance(
                    GeoPoint(emergencies.location[0], emergencies.location[1]),
                    AddressPrimaryId.addressGeoPoint!!
                )
            }
            Utils.setStatusIconColor(binding.statusIcon,emergencies.status!!)
            binding.btnDetails.isEnabled = emergencies.status != "waiting"
            binding.btnCall.isVisible = emergencies.status != "onGoing"
            binding.myEmergencyTimeRemaing.isVisible = emergencies.status != "onGoing"
            binding.clockIcon.isVisible = emergencies.status != "onGoing"
            binding.myEmergencyPhone.isVisible = true
            binding.myEmergencyName.text = emergencies.name
            binding.myEmergencyPhone.text = emergencies.phoneNumber
            binding.myEmergencyStatus.text = Utils.translateStatus(emergencies.status)
            binding.myEmergencyDate.text = Utils.formatTimestamp(emergencies.createdAt!!)
            binding.btnDetails.setOnClickListener {
                onEmergencyClicked(emergencies)
            }
            binding.btnCall.setOnClickListener {
                val dao = EmergencyDao()
                dao.updateStatusEmergency(
                    emergencies.rescuerUid!!,
                    "onGoing",
                    onSuccess = {},
                    onFailure = {})
                dao.updateStatusMyEmergency(
                    emergencies.rescuerUid,
                    "onGoing",
                    onSuccess = {},
                    onFailure = {})
                dao.updateStatusResponse(
                    emergencies.rescuerUid,
                    "onGoing",
                    onSuccess = {},
                    onFailure = {})
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${emergencies.phoneNumber}")
                ContextCompat.startActivity(binding.root.context, intent, null)
            }

            if (emergencies.status == "waiting") {
                startCountDownTimer(emergencies)
            } else {
                stopCountDownTimer()
            }
        }

        private fun startCountDownTimer(emergencies: Emergency) {
            stopCountDownTimer()

            val timerDuration = emergencies.timer * 1000L

            countDownTimer = object : CountDownTimer(timerDuration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    binding.myEmergencyTimeRemaing.text = secondsRemaining.toString()
                }

                override fun onFinish() {
                    val dao = EmergencyDao()
                    dao.updateStatusMyEmergency(
                        emergencyId = emergencies.rescuerUid.toString(),
                        "expired",
                        onSuccess = {},
                        onFailure = {})
                }
            }

            countDownTimer?.start()
        }

         fun stopCountDownTimer() {
            countDownTimer?.cancel()
            countDownTimer = null
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

    override fun onViewRecycled(holder: MyEmergencyViewHolder) {
        super.onViewRecycled(holder)
        holder.stopCountDownTimer()
    }
}