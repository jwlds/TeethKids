package com.example.teethkids.ui.adapter.recyclerviewadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.dao.NotificationDao
import com.example.teethkids.databinding.NotificationItemBinding
import com.example.teethkids.model.Notification
import com.example.teethkids.utils.Utils
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class ListNotificationsAdapter(
    private val context: Context,
    private val notifications: List<Notification>
) : RecyclerView.Adapter<ListNotificationsAdapter.NotificationViewHolder>() {


    class NotificationViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notifications: Notification) {
            binding.notificationItemBody.text = notifications.body
            binding.notificationItemDate.text = formatDate(notifications.timeStamp!!)
        }

        private fun formatDate(timestamp: Timestamp): String {
            val currentDateTime = LocalDateTime.now()
            val notificationDateTime = timestamp.toDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime()

            val dateFormat = DateTimeFormatter.ofPattern("dd/MM", Locale.getDefault())
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

            return when {
                isToday(
                    currentDateTime,
                    notificationDateTime
                ) -> "hoje às ${notificationDateTime.format(timeFormat)}"
                isYesterday(
                    currentDateTime,
                    notificationDateTime
                ) -> "ontem às ${notificationDateTime.format(timeFormat)}"
                else -> "${notificationDateTime.format(dateFormat)} às ${
                    notificationDateTime.format(
                        timeFormat
                    )
                }"
            }
        }

        private fun isToday(currentDate: LocalDateTime, notificationDate: LocalDateTime): Boolean {
            return currentDate.toLocalDate() == notificationDate.toLocalDate()
        }

        private fun isYesterday(
            currentDate: LocalDateTime,
            notificationDate: LocalDateTime
        ): Boolean {
            val yesterday = currentDate.minusDays(1)

            return yesterday.toLocalDate() == notificationDate.toLocalDate()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = NotificationItemBinding.inflate(inflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int = notifications.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notifications = notifications[position]
        holder.bind(notifications)
    }

}