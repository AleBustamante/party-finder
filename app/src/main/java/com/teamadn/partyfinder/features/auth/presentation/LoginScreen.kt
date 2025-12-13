package com.teamadn.partyfinder.features.auth.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState // Importar
import androidx.compose.foundation.verticalScroll // Importar
import androidx.compose.ui.platform.testTag // Importar IMPORTANTE
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue // <-- ¡LA IMPORTACIÓN CLAVE!
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.teamadn.partyfinder.navigation.NavigationViewModel // Asegúrate de importar esto
import com.teamadn.partyfinder.navigation.Screen
import com.teamadn.partyfinder.navigation.NavigationOptions
import androidx.compose.material3.TextButton

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    navigationViewModel: NavigationViewModel = koinViewModel()
) {
    // MODIFICADO: Consumimos los dos nuevos estados
    val fieldsState by viewModel.fieldsState.collectAsState()
    val authState by viewModel.authState.collectAsState()

    val context = LocalContext.current
    val isLoading = authState is AuthUIState.Loading

    // MODIFICADO: LaunchedEffect para reaccionar a los cambios de estado
    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthUIState.Success -> {
                // Navegar a Party y limpiar el historial para que no pueda volver al login con "Atrás"
                navigationViewModel.navigateTo(Screen.Party.route, NavigationOptions.CLEAR_BACK_STACK)
            }
            is AuthUIState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Iniciar Sesión", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = fieldsState.email, // MODIFICADO
                onValueChange = viewModel::onEmailChanged,
                label = { Text("Correo Electrónico") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("email_input"),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                enabled = !isLoading // MODIFICADO
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = fieldsState.password, // MODIFICADO
                onValueChange = viewModel::onPasswordChanged,
                label = { Text("Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("password_input"),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                enabled = !isLoading // MODIFICADO
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = viewModel::onLoginClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_button"),
                enabled = !isLoading // MODIFICADO
            ) {
                Text("Ingresar")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para ir al Registro
            TextButton(
                onClick = { navigationViewModel.navigateTo(Screen.Register.route) },
                modifier = Modifier.testTag("register_link"),
                enabled = !isLoading
            ) {
                Text("¿No tienes cuenta? Regístrate aquí")
            }
        }

        // MODIFICADO: Mostrar overlay de carga
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}