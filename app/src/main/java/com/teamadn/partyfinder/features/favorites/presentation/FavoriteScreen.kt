package com.teamadn.partyfinder.features.favorites.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teamadn.partyfinder.features.party.presentation.PartyList
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = koinViewModel()
) {
    val favorites by viewModel.favorites.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(
            text = "Mis Fiestas Favoritas",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Usamos la PartyList compartida
        PartyList(
            parties = favorites,
            // En esta pantalla, todos los items SON favoritos visualmente
            isFavorite = { true },
            // Al hacer click en el corazón, llamamos al método para quitarlo
            onToggleFavorite = { party ->
                viewModel.onRemoveFavorite(party)
            }
        )
    }
}