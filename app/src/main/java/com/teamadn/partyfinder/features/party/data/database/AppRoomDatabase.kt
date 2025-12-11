package com.teamadn.partyfinder.features.party.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.teamadn.partyfinder.features.favorites.data.database.dao.IFavoriteDao
import com.teamadn.partyfinder.features.favorites.data.database.entity.FavoritePartyEntity
import com.teamadn.partyfinder.features.party.data.database.dao.IPartyDao
import com.teamadn.partyfinder.features.party.data.database.entity.PartyEntity

@Database(entities = [PartyEntity::class, FavoritePartyEntity::class], version = 3)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun partyDao(): IPartyDao
    abstract fun favoriteDao(): IFavoriteDao // <-- Nuevo DAO

    companion object {
        @Volatile
        private var Instance: AppRoomDatabase? = null


        fun getDatabase(context: Context): AppRoomDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppRoomDatabase::class.java, "app_db")
                    .fallbackToDestructiveMigration() // Added for simplicity on version increment
                    .build()
                    .also { Instance = it }
            }
        }
    }
}