package com.example.twdist_android.features.explore.data.remote

import com.example.twdist_android.features.explore.data.dto.CreateProjectRequestDto
import com.example.twdist_android.features.explore.data.dto.ProjectDetailResponseDto
import com.example.twdist_android.features.explore.data.dto.ProjectResponseDto
import com.example.twdist_android.features.explore.data.dto.ProjectSummaryDto
import com.example.twdist_android.features.explore.data.dto.SectionResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST

interface ExploreApi {
    @GET("projects/get")
    suspend fun getProjects(): List<ProjectSummaryDto>

    @GET("projects/{projectId}/get")
    suspend fun getProjectById(
        @Path("projectId") projectId: Long
    ): ProjectDetailResponseDto

    @GET("projects/{projectId}/section/{sectionId}/get")
    suspend fun getSectionById(
        @Path("projectId") projectId: Long,
        @Path("sectionId") sectionId: Long
    ): SectionResponseDto

    @POST("projects/create")
    suspend fun createProject(
        @Body request: CreateProjectRequestDto
    ): ProjectResponseDto
}