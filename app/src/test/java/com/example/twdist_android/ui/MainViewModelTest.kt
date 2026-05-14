package com.example.twdist_android.ui

import androidx.lifecycle.SavedStateHandle
import com.example.twdist_android.data.Repository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {

    @Test
    fun `greeting delegates to repository`() {
        val repository = mockk<Repository>()
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
        every { repository.greeting() } returns "Hello from test"

        val viewModel = MainViewModel(repository, savedStateHandle)

        assertEquals("Hello from test", viewModel.greeting())
        verify(exactly = 1) { repository.greeting() }
    }
}
