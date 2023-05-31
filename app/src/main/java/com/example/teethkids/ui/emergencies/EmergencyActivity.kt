package com.example.teethkids.ui.emergencies

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.teethkids.R
import com.example.teethkids.dao.EmergecyDao
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.databinding.ActivityEmergencyBinding
import com.example.teethkids.model.ResponseEmergency
import com.example.teethkids.ui.home.MainActivity
import com.example.teethkids.utils.Utils.getCurrentDateTime
import com.example.teethkids.utils.Utils.loadImageFromUrlIv

class EmergencyActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityEmergencyBinding
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmergencyBinding.inflate(layoutInflater)
        setContentView(binding.root)






        val name = intent.getStringExtra("name")
        val phone = intent.getStringExtra("phone")
        val status = intent.getStringExtra("status")
        val dateTime = intent.getStringExtra("dateTime")

        binding.tvName.text = name
        binding.tvPhone.text = phone
        binding.tvDate.text = status
        binding.tvStatus.text = dateTime

        binding.btnCancel.setOnClickListener(this)
        binding.btnAccept.setOnClickListener(this)



        mediaPlayer = MediaPlayer.create(this, R.raw.toque_test)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()


    }


    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btnAccept -> {
                binding.loading.isVisible = true
                val id = intent.getStringExtra("id")
                val responseEmergency = ResponseEmergency(
                    professional =  getIdUser().toString(),
                    emergencyId= id.toString(),
                    status = "ACEITADO",
                    dateTime = getCurrentDateTime()
                )
                val dao = EmergecyDao()
                dao.createResponseEmergency(responseEmergency,
                    onSuccess = {
                        binding.loading.isVisible = false
                        startMainActivity()
                    },
                    onFailure = { exception ->
                        binding.loading.isVisible = false
                        startMainActivity()
                    }
                )
                startMainActivity()
            }
            R.id.btnCancel -> {
                binding.loading.isVisible = true
                val id = intent.getStringExtra("id")
                val responseEmergency = ResponseEmergency(
                    professional =  getIdUser().toString(),
                    emergencyId= id.toString(),
                    status = "REJEITADA",
                    dateTime = getCurrentDateTime()
                )
                val dao = EmergecyDao()
                dao.createResponseEmergency(responseEmergency,
                    onSuccess = {
                        binding.loading.isVisible = false
                        startMainActivity()
                    },
                    onFailure = { exception ->
                        binding.loading.isVisible = false
                        startMainActivity()
                    }
                )

            }
        }
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}