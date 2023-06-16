package com.example.teethkids.dao

import com.example.teethkids.model.Notification

class NotificationDao {

    // Função para adicionar uma notificação à lista de notificações
    fun addNotification(notification: Notification) {
        notificationList.add(notification)
    }


    companion object {
        private val notificationList = mutableListOf<Notification>()

        fun getNotificationList(): List<Notification> {
            return notificationList
        }
    }
}