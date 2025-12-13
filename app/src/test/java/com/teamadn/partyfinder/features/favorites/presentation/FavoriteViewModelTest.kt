package com.teamadn.partyfinder.features.favorites.presentation

import com.teamadn.partyfinder.MainDispatcherRule
import com.teamadn.partyfinder.features.favorites.domain.usecase.GetFavoritesUseCase
import com.teamadn.partyfinder.features.favorites.domain.usecase.ToggleFavoriteUseCase
import com.teamadn.partyfinder.features.party.domain.model.PartyModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class FavoriteViewModelTest {

    // 1. Regla para manejar las Corrutinas en el Main Thread
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // 2. Mocks de los casos de uso
    private val getFavoritesUseCase: GetFavoritesUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()

    // 3. Objeto bajo prueba (SUT - System Under Test)
    private lateinit var viewModel: FavoriteViewModel

    // Datos dummy para las pruebas
    private val dummyParty = PartyModel(
        id = "1",
        name = "Fiesta de Prueba",
        description = "Descripción",
        cost = 10.0
    )
    private val dummyList = listOf(dummyParty)

    @Test
    fun `when init, favorites state should be updated from use case`() = runTest {
        // GIVEN: El caso de uso devuelve un Flow con una lista
        every { getFavoritesUseCase() } returns flowOf(dummyList)

        // WHEN: Inicializamos el ViewModel
        viewModel = FavoriteViewModel(getFavoritesUseCase, toggleFavoriteUseCase)

        // --- SOLUCIÓN AQUÍ ---
        // Simulamos un recolector (como si fuera la UI) para activar el WhileSubscribed
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.favorites.collect()
        }
        // ---------------------

        // THEN: Ahora sí, el valor se habrá actualizado
        assertEquals(dummyList, viewModel.favorites.value)
    }

    @Test
    fun `when onRemoveFavorite is called, toggle use case should be invoked`() = runTest {
        // GIVEN: Configuramos el comportamiento del flow inicial (necesario para instanciar el VM)
        every { getFavoritesUseCase() } returns flowOf(emptyList())
        // Configuramos el toggle para que no haga nada (Unit) pero no falle
        coEvery { toggleFavoriteUseCase(any()) } returns Unit

        viewModel = FavoriteViewModel(getFavoritesUseCase, toggleFavoriteUseCase)

        // WHEN: Llamamos a la función de remover
        viewModel.onRemoveFavorite(dummyParty)

        // THEN: Verificamos que se llamó al caso de uso con el objeto correcto
        coVerify(exactly = 1) { toggleFavoriteUseCase(dummyParty) }
    }

    @Test
    fun `when init, initial state should be empty list if flow is delayed or empty`() = runTest {
        // GIVEN: El caso de uso devuelve una lista vacía
        every { getFavoritesUseCase() } returns flowOf(emptyList())

        // WHEN
        viewModel = FavoriteViewModel(getFavoritesUseCase, toggleFavoriteUseCase)

        // THEN
        assertEquals(emptyList<PartyModel>(), viewModel.favorites.value)
    }
}