package com.example.twdist_android.features.explore.data.remote

import com.example.twdist_android.features.explore.data.dto.CreateProjectRequestDto
import com.example.twdist_android.features.explore.data.dto.ProjectResponseDto
import com.example.twdist_android.features.explore.data.dto.ProjectSummaryDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExploreApi {
    @GET("projects/get")
    suspend fun getProjects(): List<ProjectSummaryDto>

    @POST("projects/create")
    suspend fun createProject(
        @Body request: CreateProjectRequestDto
    ): ProjectResponseDto

    @DELETE("projects/{projectId}/delete")
    suspend fun deleteProject(@Path("projectId") projectId: Long): Response<Unit>
}