package com.example.teethkids.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.example.teethkids.dao.UserDao
import com.example.teethkids.database.FirebaseHelper.Companion.getAuth
import com.example.teethkids.databinding.FragmentHomeBinding
import com.example.teethkids.utils.Utils
import com.example.teethkids.viewmodel.UserViewModel
import com.google.firebase.messaging.FirebaseMessaging

class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            binding.statusBar.btnStatus.isChecked = user.status
        }

        binding.btnNoti.setOnClickListener{
            requestNotificationPermission(requireContext())
        }

        binding.btnTest.setOnClickListener{
            val dao = UserDao()
            dao.fakeCall(onSuccess = {
                Utils.showToast(requireContext(),"Chamado foi")
            },
            onFailure = {
                Utils.showToast(requireContext(),"Chamado nao  foi")
            })
        }


        binding.statusBar.btnStatus.setOnCheckedChangeListener { _, isChecked ->
            val dao = UserDao()
            dao.updateStatus(getAuth().uid!!, isChecked,
                onSuccess = {
                    if(isChecked){
                        FirebaseMessaging.getInstance().subscribeToTopic("emergencies")
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("123", "Inscrito no tópico 'test' do FCM")
                                } else {
                                    Log.e("123", "Não foi possível se inscrever no tópico 'test' do FCM", task.exception)
                                }
                            }
                    } else {
                        Utils.showToast(requireContext(),"Você não está disponivel para emergências")
                    }

            })
        }




    }

    private fun requestNotificationPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Cria um canal de notificação para o Android 8.0 ou superior
            val channel = NotificationChannel(
                "channelId",
                "channelName",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "channelDescription"
            }

            // Registra o canal de notificação na NotificationManager
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Solicita a permissão de notificação
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            val intent = Intent().apply {
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
            }
            context.startActivity(intent)
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }


}