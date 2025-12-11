package com.teamadn.partyfinder.features.favorites.data.datasource

import com.teamadn.partyfinder.features.favorites.data.database.dao.IFavoriteDao
import com.teamadn.partyfinder.features.favorites.data.database.entity.FavoritePartyEntity
import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteLocalDataSource(private val dao: IFavoriteDao) {

    fun getFavorites(): Flow<List<PartyModel>> {
        return dao.getAllFavorites().map { entities ->
            entities.map {
                PartyModel(it.id, it.name, it.description, it.cost, it.style, it.time, it.imageUrl)
            }
        }
    }

    suspend fun toggleFavorite(party: PartyModel) {
        if (dao.isFavorite(party.id)) {
            dao.deleteById(party.id)
        } else {
            val entity = FavoritePartyEntity(
                party.id, party.name, party.description, party.cost, party.style, party.time, party.imageUrl
            )
            dao.insert(entity)
        }
    }

    fun isFavorite(id: String): Flow<Boolean> {
        // Un helper flow si quisieras observar un item específico (opcional)
        // Para este ejemplo usaremos la lista completa para determinar estados UI
        return kotlinx.coroutines.flow.flowOf(false)
    }
}