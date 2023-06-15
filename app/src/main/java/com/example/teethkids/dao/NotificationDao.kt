package com.example.teethkids.dao

import com.example.teethkids.model.Notification

class NotificationDao {


    fun addNotification(notification: Notification) {
        notificationList.add(notification)
    }

    fun clearNotifications() {
        notificationList.clear()
    }


    companion object {
        private val notificationList = mutableListOf<Notification>()

        fun getNotificationList(): List<Notification> {
            return notificationList
        }
    }
}