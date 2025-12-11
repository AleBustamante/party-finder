package com.teamadn.partyfinder.features.favorites.domain.repository
import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import kotlinx.coroutines.flow.Flow

interface IFavoriteRepository {
    fun getFavorites(): Flow<List<PartyModel>>
    suspend fun toggleFavorite(party: PartyModel)
}