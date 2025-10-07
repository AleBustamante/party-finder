package com.teamadn.partyfinder.features.party.domain.usecase

import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import com.teamadn.partyfinder.features.party.domain.repository.IPartyRepository
import kotlinx.coroutines.flow.Flow

class GetPartiesUseCase(private val repository: IPartyRepository) {
    operator fun invoke(): Flow<List<PartyModel>> {
        return repository.getParties()
    }
}
