package com.teamadn.partyfinder.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.teamadn.partyfinder.MainActivity
import com.teamadn.partyfinder.R
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Se llama cuando llega un mensaje y la app está en primer plano o background
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Extraer título y cuerpo, ya sea de la carga de notificación o de datos
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "Nueva Fiesta"
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: "¡Revisa las novedades!"

        showNotification(title, body)
    }

    private fun showNotification(title: String, message: String) {
        // Intent para abrir la app al tocar la notificación
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "party_channel"

        // Configuración visual de la notificación
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Usa un icono existente
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal (Obligatorio para Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Notificaciones de Fiestas",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }

    // Este método es necesario aunque no lo usemos ahora, para renovar tokens
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}