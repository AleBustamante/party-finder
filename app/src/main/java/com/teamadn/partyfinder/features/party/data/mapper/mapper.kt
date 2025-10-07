package com.teamadn.partyfinder.features.party.data.mapper

import com.teamadn.partyfinder.features.party.data.database.entity.PartyEntity
import com.teamadn.partyfinder.features.party.domain.model.PartyModel

fun PartyEntity.toModel(): PartyModel {
    return PartyModel(
        id = id,
        name = name,
        description = description,
        cost = cost,
        style = style,
        time = time,
        imageUrl = imageUrl
    )
}

fun PartyModel.toEntity(): PartyEntity {
    return PartyEntity(
        id = id,
        name = name,
        description = description,
        cost = cost,
        style = style,
        time = time,
        imageUrl = imageUrl
    )
}
