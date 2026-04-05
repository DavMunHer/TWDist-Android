package com.example.twdist_android.features.projectdetails.domain.store

import com.example.twdist_android.features.projectdetails.domain.model.Section

interface SectionStateStore {
    fun upsert(section: Section)
    fun upsertAll(sections: List<Section>)
    fun getById(sectionId: Long): Section?
    fun getByProjectId(projectId: Long): List<Section>
    fun getSectionIdsByProjectId(projectId: Long): List<Long>
    fun remove(sectionId: Long)
}
