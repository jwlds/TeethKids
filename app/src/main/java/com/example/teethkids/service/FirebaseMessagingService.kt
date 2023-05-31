package com.example.teethkids.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.NavOptions
import com.example.teethkids.R
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.Emergency
import com.example.teethkids.ui.emergencies.EmergencyActivity
import com.example.teethkids.ui.home.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONArray

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // test
    }



    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val notification = remoteMessage.notification
        val data = remoteMessage.data
        if (notification != null && data != null) {
            val title = notification.title
            val body = notification.body
            //list photos
            val photosString = data["photos"]
            val photosArray = JSONArray(photosString)
            val photosList = mutableListOf<String>()
            for (i in 0 until photosArray.length()) {
                val photo = photosArray.getString(i)
                photosList.add(photo)
            }
            //list location
            val locationString = data["location"]
            val locationArray = JSONArray(locationString)
            val locationList = mutableListOf<Double>()
            for (i in 0 until locationArray.length()) {
                val location = locationArray.getString(i)
                locationList.add(location.toDouble())
            }
            val emergencyDate = Emergency(
                emergencyId = data["id"],
                name = data["name"],
                phone = data["phone"],
                status = data["status"],
                dateTime = data["date"],
                photos = photosList,
                location = locationList
            )
            sendNotification(title, body,emergencyDate)
        }
    }



    private fun sendNotification(title: String?, body: String?,data: Emergency) {
        val bundle = Bundle().apply {
            putString("emergencyId", data.emergencyId)
            putString("name", data.name)
            putString("phone", data.phone)
            putString("status", data.status)
            putString("dateTime", data.dateTime)
            putStringArrayList("photos", ArrayList<String>(data.photos))
            val locationArray = data.location?.toDoubleArray()
            putDoubleArray("location", locationArray)
        }

        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.main_graph)
            .setDestination(R.id.emergencyDetailsFragment2)
            .setArguments(bundle)
            .setComponentName(MainActivity::class.java)
            .createPendingIntent()

        val channelId = "channel 12"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_warning_24)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, "Default", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, builder.build())
    }
}