package com.teamadn.partyfinder.features.party.data.repository

import com.teamadn.partyfinder.features.party.data.datasource.PartyLocalDataSource
import com.teamadn.partyfinder.features.party.data.datasource.PartyRealTimeRemoteDataSource
import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import com.teamadn.partyfinder.features.party.domain.repository.IPartyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class PartyRepository(
    private val remoteDataSource: PartyRealTimeRemoteDataSource,
    private val localDataSource: PartyLocalDataSource
) : IPartyRepository {

    override fun getParties(): Flow<List<PartyModel>> {
        return remoteDataSource.getPartyUpdates().onEach { parties ->
            localDataSource.deleteAll()
            localDataSource.insertParties(parties)
        }
    }
}