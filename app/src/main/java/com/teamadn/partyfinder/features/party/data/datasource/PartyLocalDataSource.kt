package com.teamadn.partyfinder.features.party.data.datasource

import com.teamadn.partyfinder.features.party.data.database.dao.IPartyDao
import com.teamadn.partyfinder.features.party.data.mapper.toEntity
import com.teamadn.partyfinder.features.party.data.mapper.toModel
import com.teamadn.partyfinder.features.party.domain.model.PartyModel

class PartyLocalDataSource(
    private val dao: IPartyDao
) {

    suspend fun getList(): List<PartyModel> {
        return dao.getList().map {
            it.toModel()
        }
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun insertParties(list: List<PartyModel>) {
        val partyEntity = list.map { it.toEntity() }
        dao.insertParties(partyEntity)
    }

    suspend fun insert(party: PartyModel) {
        dao.insert(party.toEntity())
    }
}
