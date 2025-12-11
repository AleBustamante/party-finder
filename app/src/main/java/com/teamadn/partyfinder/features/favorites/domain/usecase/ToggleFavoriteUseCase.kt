package com.teamadn.partyfinder.features.favorites.domain.usecase
import com.teamadn.partyfinder.features.favorites.domain.repository.IFavoriteRepository
import com.teamadn.partyfinder.features.party.domain.model.PartyModel

class ToggleFavoriteUseCase(private val repository: IFavoriteRepository) {
    suspend operator fun invoke(party: PartyModel) = repository.toggleFavorite(party)
}