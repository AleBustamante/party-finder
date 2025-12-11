package com.teamadn.partyfinder.features.party.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import com.teamadn.partyfinder.navigation.NavigationViewModel
import com.teamadn.partyfinder.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun PartyScreen(
    viewModel: PartyViewModel = koinViewModel(),
    navigationViewModel: NavigationViewModel // Inyectado para navegar
) {
    val state by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navigationViewModel.navigateTo(Screen.Favorites.route)
            }) {
                Icon(Icons.Default.List, contentDescription = "Ver Favoritos")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChanged,
                label = { Text("Buscar por nombre, descripción o estilo...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                singleLine = true
            )

            when (val currentState = state) {
                is PartyViewModel.PartyUIState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is PartyViewModel.PartyUIState.Success -> {
                    // Usamos la lista reutilizable.
                    // Convertimos PartyUiModel a lo que la lista necesita.
                    PartyList(
                        parties = currentState.data.map { it.party },
                        isFavorite = { partyId ->
                            // Buscamos en la lista original si es favorito
                            currentState.data.find { it.party.id == partyId }?.isFavorite == true
                        },
                        onToggleFavorite = viewModel::onToggleFavorite
                    )
                }
                is PartyViewModel.PartyUIState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: ${currentState.message}")
                    }
                }
            }
        }
    }
}

// --- COMPONENTES REUTILIZABLES (Públicos) ---

@Composable
fun PartyList(
    parties: List<PartyModel>,
    isFavorite: (String) -> Boolean, // Función para saber si una ID es favorita
    onToggleFavorite: (PartyModel) -> Unit // Acción al pulsar el corazón
) {
    if (parties.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No se encontraron fiestas.")
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(parties) { party ->
                PartyCard(
                    party = party,
                    isFavorite = isFavorite(party.id),
                    onToggleFavorite = { onToggleFavorite(party) }
                )
            }
        }
    }
}

@Composable
fun PartyCard(
    party: PartyModel,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            Column {
                // Configuración mejorada de AsyncImage
                AsyncImage(
                    model = party.imageUrl,
                    contentDescription = party.name,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray), // Color de fondo mientras carga
                    contentScale = ContentScale.Crop,
                    // Manejo de errores visual
                    onError = {
                        // Esto te ayudará a ver en el Logcat por qué falla si sigue ocurriendo
                        it.result.throwable.printStackTrace()
                    }
                )

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = party.name ?: "Sin nombre", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = party.description ?: "Sin descripción", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Costo: ${party.cost ?: "Gratis"}", style = MaterialTheme.typography.bodySmall)
                        Text(text = "Estilo: ${party.style ?: "Varios"}", style = MaterialTheme.typography.bodySmall)
                        Text(text = "Hora: ${party.time ?: "N/A"}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = Color.Black.copy(alpha = 0.5f), // Fondo semitransparente oscuro
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                IconButton(
                    onClick = onToggleFavorite
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                        tint = if (isFavorite) Color.Red else Color.White // Ahora el blanco se verá gracias al fondo oscuro
                    )
                }
            }
        }
    }
}