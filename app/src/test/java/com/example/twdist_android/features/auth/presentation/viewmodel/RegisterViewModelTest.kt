package com.example.twdist_android.features.auth.presentation.viewmodel

import com.example.twdist_android.features.auth.application.usecases.RegisterUseCase
import com.example.twdist_android.features.auth.domain.model.RegisteredUser
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val registerUseCase: RegisterUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSubmit with invalid fields sets errors and does not call use case`() = runTest {
        val viewModel = RegisterViewModel(registerUseCase)
        viewModel.updateEmail("bad")
        viewModel.updateUsername("ab")
        viewModel.updatePassword("short")
        viewModel.onSubmit()
        advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.emailError)
        coVerify(exactly = 0) { registerUseCase(any()) }
    }

    @Test
    fun `onSubmit success updates ui state`() = runTest {
        val user = RegisteredUser(id = 1L, username = "validuser", email = "u@e.com")
        coEvery { registerUseCase(any()) } returns Result.success(user)

        val viewModel = RegisterViewModel(registerUseCase)
        viewModel.updateEmail("u@e.com")
        viewModel.updateUsername("validuser")
        viewModel.updatePassword("password123")
        viewModel.onSubmit()
        advanceUntilIdle()

        coVerify(exactly = 1) { registerUseCase(any()) }
        assertTrue(viewModel.uiState.value.isSuccess)
    }
}
