package com.teamadn.partyfinder.features.favorites.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_parties")
data class FavoritePartyEntity(
    @PrimaryKey val id: String,
    val name: String?,
    val description: String?,
    val cost: Double?,
    val style: String?,
    val time: String?,
    val imageUrl: String?
)
