package com.example.twdist_android.features.projectdetails.data.store

import com.example.twdist_android.features.projectdetails.data.store.inmemory.InMemorySectionStore
import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class InMemorySectionStoreTest {

    @Test
    fun `upsert should move section id index when project id changes`() {
        val store = InMemorySectionStore()
        val sectionName = SectionName.create("Backlog").getOrThrow()
        val section = Section.create(
            id = 10L,
            projectId = 1L,
            name = sectionName,
            taskIds = emptyList()
        ).getOrThrow()
        val movedSection = Section.create(
            id = 10L,
            projectId = 2L,
            name = sectionName,
            taskIds = emptyList()
        ).getOrThrow()

        store.upsert(section)
        store.upsert(movedSection)

        assertEquals(emptyList<Long>(), store.getSectionIdsByProjectId(1L))
        assertEquals(listOf(10L), store.getSectionIdsByProjectId(2L))
    }

    @Test
    fun `remove should clear section and project index`() {
        val store = InMemorySectionStore()
        val sectionName = SectionName.create("Done").getOrThrow()
        store.upsert(
            Section.create(
                id = 20L,
                projectId = 3L,
                name = sectionName,
                taskIds = emptyList()
            ).getOrThrow()
        )

        store.remove(20L)

        assertNull(store.getById(20L))
        assertEquals(emptyList<Long>(), store.getSectionIdsByProjectId(3L))
    }
}
