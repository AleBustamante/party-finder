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
import androidx.core.content.ContextCompat


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
        currentIntent = intent

        enableEdgeToEdge()
        setContent {
            PartyFinderTheme {
                AppNavigation(navigationViewModel)
            }
        }
        Log.d("MainActivity", "onCreate - Procesando intent inicial")
        navigationViewModel.handleDeepLink(currentIntent)
        askNotificationPermission()
    }
    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Ya tengo permiso
            } else {
                // Pedir permiso
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