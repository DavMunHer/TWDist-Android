package com.example.twdist_android.features.projectdetails.application.usecases.section

import com.example.twdist_android.features.projectdetails.domain.repository.SectionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DeleteSectionUseCaseTest {
    private lateinit var useCase: DeleteSectionUseCase
    private val repository: SectionRepository = mockk()

    @Before
    fun setUp() {
        useCase = DeleteSectionUseCase(repository)
    }

    @Test
    fun `delegates to repository`() = runTest {
        coEvery { repository.deleteSection(5L) } returns Result.success(Unit)

        val result = useCase(5L)

        coVerify(exactly = 1) { repository.deleteSection(5L) }
        assertTrue(result.isSuccess)
    }
}
