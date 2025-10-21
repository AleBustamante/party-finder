package com.teamadn.partyfinder.features.party.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

import coil3.compose.AsyncImage
import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PartyScreen(viewModel: PartyViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()
    // MODIFICADO: Se obtiene el estado de la consulta de búsqueda.
    val searchQuery by viewModel.searchQuery.collectAsState()

    // MODIFICADO: Se envuelve el contenido en un `Column` para agregar el campo de búsqueda.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // MODIFICADO: Se añade un campo de texto para la búsqueda.
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
                PartyList(parties = currentState.data)
            }
            is PartyViewModel.PartyUIState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Error: ${currentState.message}")
                }
            }
        }
    }
}


@Composable
fun PartyList(parties: List<PartyModel>) {
    // MODIFICADO: Se ajusta el padding para que el LazyColumn no tenga padding superior.
    if (parties.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No se encontraron fiestas.")
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(parties) { party ->
                PartyCard(party = party)
            }
        }
    }
}
@Composable
fun PartyCard(party: PartyModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            party.imageUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = party.name,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = party.name ?: "", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = party.description ?: "", style = MaterialTheme.typography.bodyMedium)
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
    }
}