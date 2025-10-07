package com.teamadn.partyfinder.features.party.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parties")
data class PartyEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "cost")
    var cost: Double? = null,

    @ColumnInfo(name = "style")
    var style: String? = null,

    @ColumnInfo(name = "time")
    var time: String? = null,

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String? = null
)
