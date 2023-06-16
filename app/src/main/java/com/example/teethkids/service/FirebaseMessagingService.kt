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
import com.example.teethkids.dao.NotificationDao
import com.example.teethkids.database.FirebaseHelper.Companion.getDatabase
import com.example.teethkids.database.FirebaseHelper.Companion.getIdUser
import com.example.teethkids.model.Emergency
import com.example.teethkids.model.Notification
import com.example.teethkids.ui.auth.AuthenticateActivity
import com.example.teethkids.ui.home.MainActivity
import com.example.teethkids.utils.Utils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.type.Date
import org.json.JSONArray
import org.json.JSONObject

class FirebaseMessagingService : FirebaseMessagingService() {


    //Quando um novo  fcmToken é criado chama a funcão "sendRegistrationToServer" para atualizar o token do usuário.
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
        val data = remoteMessage.data

        if (notification != null) {
            val dao = NotificationDao()
            dao.addNotification(Notification(notification.title, notification.body))


            // 40 --> Notificação de review.
            // 50 --> Notificação de emergencia. (Quando ela é criada na coleção "emergencies").
            // 60 --> Notificação que o socorrista escolheu o usuario atual (Dentista), (Quando ela é criada na sub coleção "MyEmergencies").

            // Verifica o valor do campo "key" no data  para mostra notificações difirentes.
            when (data["key"]) {
                "40" -> sendNotificationReview(notification.title, notification.body)
                "50" -> {

                    //Transforma as url das  fotos que estão em formato JSON em uma lista. (Não foi conseguir enviá-las como um array na função da mensagem, apenas como JSON).
                    val photosString = data["photos"]
                    val photosArray = JSONArray(photosString)
                    val photosList = mutableListOf<String>()
                    for (i in 0 until photosArray.length()) {
                        val photo = photosArray.getString(i)
                        photosList.add(photo)
                    }


                    //Transforma as lat e lon  que estão em formato JSON em uma lista.
                    val locationString = data["location"]
                    val locationArray = JSONArray(locationString)
                    val locationList = mutableListOf<Double>()
                    for (i in 0 until locationArray.length()) {
                        val location = locationArray.getString(i)
                        locationList.add(location.toDouble())
                    }

                    val createdAtString = data["createdAt"]
                    val createdAtJson = JSONObject(createdAtString)
                    val seconds = createdAtJson.getLong("_seconds")
                    val nanoseconds = createdAtJson.getInt("_nanoseconds")
                    val createdAt = com.google.firebase.Timestamp(seconds, nanoseconds)

                    // Cria um objeto Emergency com as informações recebidas
                    val emergencyDate = Emergency(
                        rescuerUid = data["rescuerUid"],
                        name = data["name"],
                        status = data["status"],
                        createdAt = createdAt,
                        photos = photosList,
                        location = locationList
                    )

                    sendNotification(notification.title, notification.body, emergencyDate)
                }
                "60" -> sendNotificationMyEmergency(notification.title, notification.body)
            }
        }
    }





    // Criar e exibir uma notificação de emergencia com base nos dados recebidos.
    private fun sendNotification(title: String?, body: String?, data: Emergency) {

        // Criar um bundle com informações da emergencia.
        val bundle = Bundle().apply {
            putString("emergencyId", data.rescuerUid)
            putString("name", data.name)
            putString("status", data.status)
            putString("createdAt", Utils.formatTimestamp(data.createdAt!!))
            putStringArrayList("photos", ArrayList<String>(data.photos))
            val locationArray = data.location?.toDoubleArray()
            putDoubleArray("location", locationArray)
        }

        // para abrir o fragmento "EmergencyDetailsFragment" quando for clicado.
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
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, "Default", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, builder.build())
    }


    // Criar e exibir uma notificação de review com base nos dados recebidos.
    private fun sendNotificationReview(title: String?, body: String?) {
        val channelId = "channel 12"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.outline_reviews_24)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        // Para abrir o fragmento  "AuthenticateActivity"  quando for clicado.
        val intent = Intent(this, AuthenticateActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        builder.setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, "Default", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, builder.build())
    }


    // Criar e exibir uma notificação das minhas emergencias com base nos dados recebidos.
    private fun sendNotificationMyEmergency(title: String?, body: String?) {
        val channelId = "channel 12"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_warning_24)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)


        // Para abrir o fragmento  "AuthenticateActivity"  quando for clicado.
        val intent = Intent(this, AuthenticateActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        builder.setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, "Default", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(0, builder.build())
    }


}