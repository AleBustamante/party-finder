package com.teamadn.partyfinder.features.party.data.datasource

import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PartyRealTimeRemoteDataSource {

    fun getPartyUpdates(): Flow<List<PartyModel>> = callbackFlow {
        val database = Firebase.database
        val myRef = database.getReference("parties")

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val parties = snapshot.children.mapNotNull { it.getValue(PartyModel::class.java) }
                trySend(parties)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        myRef.addValueEventListener(valueEventListener)

        awaitClose { myRef.removeEventListener(valueEventListener) }
    }
}
