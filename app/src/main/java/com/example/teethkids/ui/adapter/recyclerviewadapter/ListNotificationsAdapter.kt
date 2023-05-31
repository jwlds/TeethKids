package com.example.teethkids.ui.adapter.recyclerviewadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teethkids.databinding.NotificationItemBinding
import com.example.teethkids.model.Notification
import java.time.LocalDate
import java.time.LocalDateTime
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
            binding.notificationItemDate.text = formatDate(notifications.data)
        }

        private fun formatDate(data: LocalDate): String {
            val currentDate = LocalDateTime.now()
            val notificationDate = data.atStartOfDay()

            val dateFormat = DateTimeFormatter.ofPattern("dd/MM", Locale.getDefault())
            val timeFormat = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

            return when {
                isToday(currentDate, notificationDate) -> "hoje às ${notificationDate.format(timeFormat)}"
                isYesterday(currentDate, notificationDate) -> "ontem às ${notificationDate.format(timeFormat)}"
                else -> "${notificationDate.format(dateFormat)} às ${notificationDate.format(timeFormat)}"
            }
        }

        private fun isToday(currentDate: LocalDateTime, notificationDate: LocalDateTime): Boolean {
            return currentDate.toLocalDate() == notificationDate.toLocalDate()
        }

        private fun isYesterday(currentDate: LocalDateTime, notificationDate: LocalDateTime): Boolean {
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
        val contact = notifications[position]
        holder.bind(contact)
    }

}