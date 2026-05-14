package com.example.twdist_android.features.auth.presentation.viewmodel

import com.example.twdist_android.features.auth.application.usecases.LoginUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val loginUseCase: LoginUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSubmit with invalid email sets field error and does not call use case`() = runTest {
        val viewModel = LoginViewModel(loginUseCase)
        viewModel.updateEmail("not-an-email")
        viewModel.updatePassword("password123")
        viewModel.onSubmit()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.emailError)
        coVerify(exactly = 0) { loginUseCase(any()) }
    }

    @Test
    fun `onSubmit with valid form calls login use case and sets success`() = runTest {
        coEvery { loginUseCase(any()) } returns Unit

        val viewModel = LoginViewModel(loginUseCase)
        viewModel.updateEmail("user@email.com")
        viewModel.updatePassword("password123")
        viewModel.onSubmit()
        advanceUntilIdle()

        coVerify(exactly = 1) { loginUseCase(any()) }
        assertTrue(viewModel.uiState.value.isSuccess)
        assertNull(viewModel.uiState.value.errorMessage)
    }
}
