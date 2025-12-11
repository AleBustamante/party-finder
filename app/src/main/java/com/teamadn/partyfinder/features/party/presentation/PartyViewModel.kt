package com.teamadn.partyfinder.features.party.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamadn.partyfinder.features.favorites.domain.usecase.GetFavoritesUseCase
import com.teamadn.partyfinder.features.favorites.domain.usecase.ToggleFavoriteUseCase
import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import com.teamadn.partyfinder.features.party.domain.usecase.GetPartiesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PartyViewModel(
    private val getPartiesUseCase: GetPartiesUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase, // Nuevo
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase // Nuevo
) : ViewModel() {

    // Wrapper para la UI que incluye si es favorito
    data class PartyUiModel(
        val party: PartyModel,
        val isFavorite: Boolean
    )

    sealed class PartyUIState {
        object Loading : PartyUIState()
        class Error(val message: String) : PartyUIState()
        class Success(val data: List<PartyUiModel>) : PartyUIState() // Cambio a UiModel
    }

    private val _uiState = MutableStateFlow<PartyUIState>(PartyUIState.Loading)
    val uiState: StateFlow<PartyUIState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        fetchAndFilterParties()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onToggleFavorite(party: PartyModel) {
        viewModelScope.launch {
            toggleFavoriteUseCase(party)
        }
    }

    private fun fetchAndFilterParties() {
        viewModelScope.launch(Dispatchers.IO) {
            // Combinamos 3 flujos: Fiestas, Búsqueda y Favoritos
            combine(
                getPartiesUseCase(),
                _searchQuery,
                getFavoritesUseCase()
            ) { parties, query, favorites ->

                // Set de IDs de favoritos para búsqueda rápida O(1)
                val favoriteIds = favorites.map { it.id }.toSet()

                val filtered = if (query.isBlank()) {
                    parties
                } else {
                    parties.filter { party ->
                        val queryLower = query.lowercase()
                        (party.name?.lowercase()?.contains(queryLower) == true) ||
                                (party.description?.lowercase()?.contains(queryLower) == true) ||
                                (party.style?.lowercase()?.contains(queryLower) == true)
                    }
                }

                // Mapeamos a PartyUiModel
                filtered.map { party ->
                    PartyUiModel(party, isFavorite = favoriteIds.contains(party.id))
                }
            }
                .catch { e ->
                    _uiState.value = PartyUIState.Error(e.message ?: "Unknown Error")
                }
                .collect { mappedParties ->
                    _uiState.value = PartyUIState.Success(mappedParties)
                }
        }
    }
}