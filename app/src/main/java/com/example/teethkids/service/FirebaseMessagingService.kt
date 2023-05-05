package com.example.teethkids.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.Global.putString
import android.util.Log
import androidx.core.app.NotificationCompat
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
        updateFcmToken(token)
    }

     private fun updateFcmToken(newToken: String) {
        val userId = getIdUser() // supondo que você tenha essa função definida em algum lugar para recuperar o ID do usuário atualmente autenticado
        if (userId != null) {
            val userRef = FirebaseFirestore.getInstance().collection("profiles").document(userId)
            userRef.update("fcmToken", newToken)
                .addOnSuccessListener {
                    Log.d("222", "FCM token updated successfully")
                }
                .addOnFailureListener {
                    Log.e("222", "Error updating FCM token", it)
                }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val notification = remoteMessage.notification
        val data = remoteMessage.data
        if (notification != null && data != null) {
            val title = notification.title
            val body = notification.body
            val photosString = data["photos"]
            val photosArray = JSONArray(photosString)
            val photosList = mutableListOf<String>()
            for (i in 0 until photosArray.length()) {
                val photo = photosArray.getString(i)
                photosList.add(photo)
            }
            val emergencyDate = Emergency(
                emergencyId = data["id"],
                name = data["name"],
                phone = data["phone"],
                status = data["status"],
                dateTime = data["date"],
                photo1 = photosList[0],
                photo2 = photosList[1]
            )
            sendNotification(title, body,emergencyDate)
        }
    }



    private fun sendNotification(title: String?, body: String?,data: Emergency) {
        val intent = Intent(this, EmergencyActivity::class.java)
        intent.putExtra("id",data.emergencyId)
        intent.putExtra("name",data.name)
        intent.putExtra("phone",data.phone)
        intent.putExtra("status",data.status)
        intent.putExtra("dateTime",data.dateTime)
        intent.putExtra("photo1",data.photo1)
        intent.putExtra("photo2",data.photo2)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
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