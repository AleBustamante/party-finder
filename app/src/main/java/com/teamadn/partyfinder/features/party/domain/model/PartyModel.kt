package com.teamadn.partyfinder.features.party.domain.model

data class PartyModel(
    val id: String = "",
    val name: String? = null,
    val description: String? = null,
    val cost: Double? = null,
    val style: String? = null,
    val time: String? = null,
    val imageUrl: String? = null
)
