package com.teamadn.partyfinder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.teamadn.partyfinder.navigation.AppNavigation
import com.teamadn.partyfinder.navigation.NavigationViewModel
import com.teamadn.partyfinder.ui.theme.PartyFinderTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.teamadn.partyfinder.features.common.presentation.MaintenanceScreen

class MainActivity : ComponentActivity() {
    private val navigationViewModel: NavigationViewModel by viewModel()
    private var currentIntent: Intent? = null


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("FCM", "Permiso de notificaciones concedido")
        } else {
            Log.d("FCM", "Permiso de notificaciones denegado")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Configuración inicial (Solo una vez)
        askNotificationPermission()
        enableEdgeToEdge()

        // 2. Suscripción a FCM
        FirebaseMessaging.getInstance().subscribeToTopic("general")
            .addOnCompleteListener { task ->
                var msg = "Suscrito a notificaciones generales"
                if (!task.isSuccessful) {
                    msg = "Falló la suscripción"
                }
                Log.d("FCM", msg)
            }

        // 3. UI (UN SOLO setContent)
        setContent {
            PartyFinderTheme {
                // Observamos el estado del Remote Config
                val isMaintenance by navigationViewModel.isMaintenanceMode.collectAsState()

                when (isMaintenance) {
                    true -> {
                        // Si remote config dice true, mostramos pantalla de bloqueo
                        MaintenanceScreen()
                    }
                    false -> {
                        // Si es false, mostramos la app normal
                        AppNavigation(navigationViewModel)
                    }
                    null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }

        // 4. Manejo de Intents (Deep Links)
        // Esto se ejecuta después de configurar la UI, lo cual es correcto para procesar la data
        currentIntent = intent
        Log.d("MainActivity", "onCreate - Procesando intent inicial")
        navigationViewModel.handleDeepLink(currentIntent)
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d("MainActivity", "onNewIntent llamado")

        this.intent = intent
        currentIntent = intent

        navigationViewModel.handleDeepLink(intent)
    }
}