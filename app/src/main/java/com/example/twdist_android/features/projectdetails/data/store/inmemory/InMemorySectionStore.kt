package com.example.twdist_android.features.projectdetails.data.store.inmemory

import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.store.SectionStateStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemorySectionStore @Inject constructor() : SectionStateStore {
    private val sectionsById = linkedMapOf<Long, Section>()
    private val sectionIdsByProjectId = linkedMapOf<Long, MutableSet<Long>>()

    // Private, no lock. Call only from code paths that already hold the monitor.
    private fun upsertInternal(section: Section) {
        val existing = sectionsById[section.id]
        if (existing != null && existing.projectId != section.projectId) {
            sectionIdsByProjectId[existing.projectId]?.remove(existing.id)
        }

        sectionsById[section.id] = section
        val projectSet = sectionIdsByProjectId.getOrPut(section.projectId) { linkedSetOf() }
        projectSet.add(section.id)
    }

    override fun upsert(section: Section) {
        synchronized(this) {
            upsertInternal(section)
        }
    }

    override fun upsertAll(sections: List<Section>) {
        synchronized(this) {
            sections.forEach { section -> upsertInternal(section) }
        }
    }

    override fun getById(sectionId: Long): Section? {
        return synchronized(this) { sectionsById[sectionId] }
    }

    override fun getByProjectId(projectId: Long): List<Section> {
        return synchronized(this) {
            val ids = sectionIdsByProjectId[projectId].orEmpty()
            ids.mapNotNull { id -> sectionsById[id] }
        }
    }

    override fun getSectionIdsByProjectId(projectId: Long): List<Long> {
        return synchronized(this) {
            sectionIdsByProjectId[projectId].orEmpty().toList()
        }
    }

    override fun remove(sectionId: Long) {
        synchronized(this) {
            val removed = sectionsById.remove(sectionId) ?: return
            sectionIdsByProjectId[removed.projectId]?.remove(removed.id)
            if (sectionIdsByProjectId[removed.projectId]?.isEmpty() == true) {
                sectionIdsByProjectId.remove(removed.projectId)
            }
        }
    }
}
