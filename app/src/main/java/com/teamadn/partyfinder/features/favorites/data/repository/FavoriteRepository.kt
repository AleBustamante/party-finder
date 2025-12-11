package com.teamadn.partyfinder.features.favorites.data.repository

import com.teamadn.partyfinder.features.favorites.data.datasource.FavoriteLocalDataSource
import com.teamadn.partyfinder.features.favorites.domain.repository.IFavoriteRepository
import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(
    private val localDataSource: FavoriteLocalDataSource
) : IFavoriteRepository {
    override fun getFavorites(): Flow<List<PartyModel>> = localDataSource.getFavorites()
    override suspend fun toggleFavorite(party: PartyModel) = localDataSource.toggleFavorite(party)
}