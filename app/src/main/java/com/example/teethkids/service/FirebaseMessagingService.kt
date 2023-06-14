package com.example.teethkids.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.teethkids.R
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.Emergency
import com.example.teethkids.ui.auth.AuthenticateActivity
import com.example.teethkids.ui.home.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONArray

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("sendRegistrationToServer", "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        val authUid = getIdUser()
        if (authUid != null) {
            val userRef = getDatabase().collection("profiles").document(authUid)
            userRef.update("fcmToken", token)
                .addOnSuccessListener {
                    Log.d("sendRegistrationToServer", "Refreshed token: $token")
                }
                .addOnFailureListener { exception ->
                    Log.d("sendRegistrationToServer", "Erro Refreshed token: $exception")
                }
        }
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val notification = remoteMessage.notification

        if(notification != null) {
            sendNotificationReview(notification.title,notification.body)
        }
           // sendNotification(title, body,emergencyDate)
        }
    private fun sendNotification(title: String?, body: String?,data: Emergency) {
        val bundle = Bundle().apply {
            putString("emergencyId", data.rescuerUid)
            putString("name", data.name)
            putString("phone", data.phoneNumber)
            putString("status", data.status)
            //   putString("dateTime", data.dateTime)
            putStringArrayList("photos", ArrayList<String>(data.photos))
            //  val locationArray = data.location?.toDoubleArray()
            //  putDoubleArray("location", locationArray)
        }

        val pendingIntent = NavDeepLinkBuilder(this)
            .setGraph(R.navigation.main_graph)
            .setDestination(R.id.emergencyDetailsFragment2)
            .setArguments(bundle)
            .setComponentName(MainActivity::class.java)
            .createPendingIntent()

        val channelId = "channel 12"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.outline_reviews_24)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, "Default", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, builder.build())
    }

    private fun sendNotificationReview(title: String?, body: String?) {
        val channelId = "channel 12"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.outline_reviews_24)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        // Crie o Intent para abrir a AuthActivity
        val intent = Intent(this, AuthenticateActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE) // Adicione a flag FLAG_IMMUTABLE aqui

        builder.setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(channelId, "Default", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, builder.build())
    }


    }