package com.teamadn.partyfinder

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun verifyLoginScreenElementsAreVisible() {
        // Verificamos por texto el título (esto está bien para textos estáticos)
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()

        // Verificamos por TAG los inputs (esto es más seguro)
        composeTestRule.onNodeWithTag("email_input").assertIsDisplayed()
        composeTestRule.onNodeWithTag("password_input").assertIsDisplayed()
        composeTestRule.onNodeWithTag("login_button").assertIsDisplayed()
    }

    @Test
    fun verifyUserCanEnterCredentials() {
        // 1. Escribir en el campo de correo usando TAG
        composeTestRule.onNodeWithTag("email_input")
            .performTextInput("usuario@prueba.com")

        // 2. Escribir en contraseña usando TAG
        composeTestRule.onNodeWithTag("password_input")
            .performTextInput("123456")

        // 3. Verificar que el texto existe en la jerarquía (dentro del input)
        composeTestRule.onNodeWithText("usuario@prueba.com").assertExists()
    }

    @Test
    fun verifyNavigationToRegisterScreen() {
        // 1. Buscar el botón por TAG y hacer scroll hasta él
        // Ahora funcionará porque agregamos .verticalScroll() en la LoginScreen
        composeTestRule.onNodeWithTag("register_link")
            .performScrollTo()
            .performClick()

        // 2. Esperar que aparezca la pantalla de registro
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            try {
                composeTestRule.onNodeWithText("Crear Cuenta").assertExists()
                true
            } catch (e: AssertionError) {
                false
            }
        }

        composeTestRule.onNodeWithText("Crear Cuenta").assertIsDisplayed()
    }
}