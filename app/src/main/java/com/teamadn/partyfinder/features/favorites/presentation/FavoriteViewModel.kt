package com.teamadn.partyfinder.features.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamadn.partyfinder.features.favorites.domain.usecase.GetFavoritesUseCase
import com.teamadn.partyfinder.features.favorites.domain.usecase.ToggleFavoriteUseCase
import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    // Usamos stateIn para convertir el Flow en StateFlow directamente
    val favorites: StateFlow<List<PartyModel>> = getFavoritesUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onRemoveFavorite(party: PartyModel) {
        viewModelScope.launch {
            toggleFavoriteUseCase(party)
        }
    }
}