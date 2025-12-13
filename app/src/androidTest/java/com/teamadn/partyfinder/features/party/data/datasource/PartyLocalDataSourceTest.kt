package com.teamadn.partyfinder.features.party.data.datasource

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.teamadn.partyfinder.features.party.data.database.AppRoomDatabase
import com.teamadn.partyfinder.features.party.data.database.dao.IPartyDao
import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class PartyLocalDataSourceTest {

    private lateinit var db: AppRoomDatabase
    private lateinit var partyDao: IPartyDao
    private lateinit var localDataSource: PartyLocalDataSource

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Usamos una DB en memoria: se borra al terminar el test
        db = Room.inMemoryDatabaseBuilder(
            context, AppRoomDatabase::class.java
        ).build()

        partyDao = db.partyDao()

        // Instanciamos el DataSource manualmente pasando el DAO de prueba
        localDataSource = PartyLocalDataSource(partyDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun writePartyAndReadInList() = runTest {
        // 1. Arrange (Preparar datos)
        val party = PartyModel(
            id = "1",
            name = "Fiesta de Integración",
            description = "Prueba de base de datos",
            cost = 50.0,
            style = "Tech",
            time = "22:00",
            imageUrl = "http://fake.url/img.jpg"
        )

        // 2. Act (Ejecutar insert)
        localDataSource.insert(party)

        // 3. Assert (Verificar lectura)
        val parties = localDataSource.getList()

        // Verificamos que la lista no esté vacía
        assertTrue(parties.isNotEmpty())
        // Verificamos que el primer elemento coincida con lo insertado
        assertEquals(party.name, parties[0].name)
        assertEquals(party.cost, parties[0].cost)
    }

    @Test
    fun insertListAndClearAll() = runTest {
        // 1. Arrange
        val partyList = listOf(
            PartyModel(id = "1", name = "Party A"),
            PartyModel(id = "2", name = "Party B")
        )

        // 2. Act (Insertar lista)
        localDataSource.insertParties(partyList)
        val resultAfterInsert = localDataSource.getList()

        // 3. Act (Borrar todo)
        localDataSource.deleteAll()
        val resultAfterDelete = localDataSource.getList()

        // 4. Assert
        assertEquals(2, resultAfterInsert.size)
        assertTrue(resultAfterDelete.isEmpty())
    }

    @Test
    fun conflictStrategyShouldReplaceData() = runTest {
        // Probamos que si insertamos el mismo ID, se actualice y no duplique

        // 1. Insertar versión original
        val original = PartyModel(id = "99", name = "Original Name", cost = 10.0)
        localDataSource.insert(original)

        // 2. Insertar versión actualizada con el MISMO ID
        val updated = PartyModel(id = "99", name = "Updated Name", cost = 20.0)
        localDataSource.insert(updated)

        // 3. Verificar
        val list = localDataSource.getList()

        assertEquals(1, list.size) // No debe haber 2 elementos
        assertEquals("Updated Name", list[0].name) // El nombre debió cambiar
        assertEquals(20.0, list[0].cost)
    }
}