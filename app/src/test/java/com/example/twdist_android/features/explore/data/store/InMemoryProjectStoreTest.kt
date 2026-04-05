package com.example.twdist_android.features.explore.data.store

import com.example.twdist_android.features.explore.data.store.inmemory.InMemoryProjectStore
import com.example.twdist_android.features.projectdetails.domain.model.ProjectName
import com.example.twdist_android.features.explore.domain.model.ProjectSummary
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class InMemoryProjectStoreTest {

    @Test
    fun `upsert should replace existing summary with same id`() {
        val store = InMemoryProjectStore()
        val name = ProjectName.create("Inbox").getOrThrow()
        val first = ProjectSummary(id = 1L, name = name, isFavorite = false, pendingTasks = 1)
        val updated = ProjectSummary(id = 1L, name = name, isFavorite = true, pendingTasks = 5)

        store.upsert(first)
        store.upsert(updated)

        assertEquals(updated, store.getById(1L))
        assertEquals(listOf(updated), store.getAll())
    }

    @Test
    fun `remove should delete summary from store`() {
        val store = InMemoryProjectStore()
        val name = ProjectName.create("Inbox").getOrThrow()
        val project = ProjectSummary(id = 7L, name = name, isFavorite = false, pendingTasks = 0)

        store.upsert(project)
        store.remove(7L)

        assertNull(store.getById(7L))
        assertEquals(emptyList<ProjectSummary>(), store.getAll())
    }
}
