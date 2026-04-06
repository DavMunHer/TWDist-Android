package com.example.twdist_android.features.projectdetails.domain.store

import com.example.twdist_android.features.projectdetails.domain.model.Section
import com.example.twdist_android.features.projectdetails.domain.model.SectionName

interface SectionStateStore {
    fun upsert(section: Section)
    fun upsertAll(sections: List<Section>)
    fun getById(sectionId: Long): Section?
    fun getByProjectId(projectId: Long): List<Section>
    fun getSectionIdsByProjectId(projectId: Long): List<Long>
    fun updateName(sectionId: Long, sectionName: SectionName): Section?
    fun remove(sectionId: Long)
}
