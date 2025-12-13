package com.teamadn.partyfinder.features.party.domain.usecase

import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import com.teamadn.partyfinder.features.party.domain.repository.IPartyRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetPartiesUseCaseTest {

    private val repository: IPartyRepository = mockk()
    private val getPartiesUseCase = GetPartiesUseCase(repository)

    @Test
    fun `invoke should return flow of parties from repository`() = runBlocking {
        // GIVEN
        val parties = listOf(
            PartyModel(id = "1", name = "Fiesta A"),
            PartyModel(id = "2", name = "Fiesta B")
        )
        // Simulamos que el repo devuelve un Flow con esa lista
        every { repository.getParties() } returns flowOf(parties)

        // WHEN
        val resultFlow = getPartiesUseCase()
        val resultList = resultFlow.first() // Obtenemos el primer valor emitido

        // THEN
        assertEquals(2, resultList.size)
        assertEquals("Fiesta A", resultList[0].name)
        verify(exactly = 1) { repository.getParties() }
    }
}