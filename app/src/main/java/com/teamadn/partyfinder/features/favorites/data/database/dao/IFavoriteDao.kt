package com.teamadn.partyfinder.features.favorites.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teamadn.partyfinder.features.favorites.data.database.entity.FavoritePartyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IFavoriteDao {
    @Query("SELECT * FROM favorite_parties")
    fun getAllFavorites(): Flow<List<FavoritePartyEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_parties WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(party: FavoritePartyEntity)

    @Query("DELETE FROM favorite_parties WHERE id = :id")
    suspend fun deleteById(id: String)
}