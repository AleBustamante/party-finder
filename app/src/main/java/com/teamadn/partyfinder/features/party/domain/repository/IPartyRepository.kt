package com.teamadn.partyfinder.features.party.domain.repository

import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import kotlinx.coroutines.flow.Flow

interface IPartyRepository {
    fun getParties(): Flow<List<PartyModel>>
}
