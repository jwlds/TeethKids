package com.example.teethkids.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teethkids.R
import com.example.teethkids.databinding.FragmentNotificationBinding
import com.example.teethkids.model.Notification
import com.example.teethkids.ui.adapter.recyclerviewadapter.ListNotificationsAdapter
import java.time.LocalDate


class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notifications = createFakeNotifications()

        val adapter = ListNotificationsAdapter(requireContext(),notifications)
        binding.listNotifications.adapter = adapter
        binding.listNotifications.layoutManager = LinearLayoutManager(requireContext())
        binding.toolbar.btnBack.isVisible = false
        binding.toolbar.screenName.text = "Notificações"
    }

    private fun createFakeNotifications(): List<Notification> {
        val notifications = mutableListOf<Notification>()
        val currentDate = LocalDate.now()
        val yesterday = LocalDate.now().minusDays(1)
        val anotherDay = LocalDate.now().minusDays(2)

        notifications.add(Notification("Você recebeu o chamado de uma ocorrência.", currentDate))
        notifications.add(
            Notification(
                "O chamado foi canelado pelo socorrista.",
                yesterday
            )
        )
        notifications.add(Notification("O socorrista aceitou sua confirmação.", anotherDay))
        notifications.add(Notification("O socorrista compartilhou com você sua localização.", anotherDay))
        notifications.add(Notification("Você recebeu uma avaliação.", anotherDay))
        notifications.add(Notification("Sua avaliação foi para revisão.", anotherDay))

        return notifications
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}