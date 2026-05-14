package com.example.twdist_android.features.explore.data.repository

import com.example.twdist_android.core.data.local.TWDistDatabase
import com.example.twdist_android.core.data.local.dao.ProjectDao
import com.example.twdist_android.features.explore.data.dto.ProjectSummaryDto
import com.example.twdist_android.features.explore.data.remote.ExploreApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProjectRepositoryImplTest {

    @Test
    fun `getAllProjects maps api results and upserts into dao`() = runTest {
        val api = mockk<ExploreApi>()
        val dao = mockk<ProjectDao>()
        val db = mockk<TWDistDatabase>()
        every { db.projectDao() } returns dao

        val dto = ProjectSummaryDto(id = "42", name = "Work", favorite = false, pendingCount = 3)
        coEvery { api.getProjects() } returns listOf(dto)
        coEvery { dao.upsertAll(any()) } just Runs

        val repository = ProjectRepositoryImpl(api, db)
        val result = repository.getAllProjects()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
        assertEquals(42L, result.getOrThrow()[0].id)
        coVerify(exactly = 1) { dao.upsertAll(any()) }
    }
}
