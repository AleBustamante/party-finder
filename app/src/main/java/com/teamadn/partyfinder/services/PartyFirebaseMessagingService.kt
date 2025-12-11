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

class PartyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Aquí podrías enviar el token a tu base de datos si necesitas enviar notificaciones a usuarios específicos.
        // Por ejemplo: userRepository.updatePushToken(token)
        // Como tu app usa Auth, podrías guardar esto en el nodo del usuario en Firebase Realtime Database.
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Las notificaciones pueden venir con datos (data payload) o notificación visual (notification payload).
        // Si la app está en primer plano, este método se dispara para ambos.

        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "Nueva Fiesta"
        val message = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: "¡Revisa las nuevas fiestas disponibles!"

        showNotification(title, message)
    }

    private fun showNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        // PendingIntent para abrir la app al tocar la notificación
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "party_finder_updates"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Asegúrate de tener un icono válido, usa el del sistema por ahora
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal de notificación para Android O y superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Actualizaciones de Fiestas",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }
}