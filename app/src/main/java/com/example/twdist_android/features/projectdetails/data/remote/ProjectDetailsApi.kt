package com.example.twdist_android.features.projectdetails.data.remote

import com.example.twdist_android.features.projectdetails.data.dto.ProjectDetailResponseDto
import com.example.twdist_android.features.projectdetails.data.dto.CreateTaskRequestDto
import com.example.twdist_android.features.projectdetails.data.dto.CompleteTaskRequestDto
import com.example.twdist_android.features.projectdetails.data.dto.SectionUpdateResponseDto
import com.example.twdist_android.features.projectdetails.data.dto.TaskResponseDto
import com.example.twdist_android.features.projectdetails.data.dto.UpdateProjectRequestDto
import com.example.twdist_android.features.projectdetails.data.dto.UpdateSectionRequestDto
import com.example.twdist_android.features.projectdetails.data.dto.UpdateTaskRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT

interface ProjectDetailsApi {
    @GET("projects/{projectId}")
    suspend fun getProjectById(
        @Path("projectId") projectId: Long
    ): ProjectDetailResponseDto

    @PUT("projects/{projectId}/update")
    suspend fun updateProject(
        @Path("projectId") projectId: Long,
        @Body request: UpdateProjectRequestDto
    ): Response<ProjectDetailResponseDto>

    @DELETE("projects/{projectId}/delete")
    suspend fun deleteProject(
        @Path("projectId") projectId: Long
    ): Response<Unit>

    @PUT("projects/{projectId}/section/{sectionId}/update")
    suspend fun updateSection(
        @Path("projectId") projectId: Long,
        @Path("sectionId") sectionId: Long,
        @Body request: UpdateSectionRequestDto
    ): SectionUpdateResponseDto

    @POST("projects/{projectId}/section/create")
    suspend fun createSection(
        @Path("projectId") projectId: Long,
        @Body request: UpdateSectionRequestDto
    ): SectionUpdateResponseDto

    @DELETE("projects/{projectId}/section/{sectionId}/delete")
    suspend fun deleteSection(
        @Path("projectId") projectId: Long,
        @Path("sectionId") sectionId: Long
    ): Response<Unit>

    @POST("projects/{projectId}/section/{sectionId}/task/create")
    suspend fun createTask(
        @Path("projectId") projectId: Long,
        @Path("sectionId") sectionId: Long,
        @Body request: CreateTaskRequestDto
    ): Response<TaskResponseDto>

    @GET("projects/{projectId}/section/{sectionId}/task/get")
    suspend fun getTasksBySection(
        @Path("projectId") projectId: Long,
        @Path("sectionId") sectionId: Long
    ): Response<List<TaskResponseDto>>

    @PUT("projects/{projectId}/section/{sectionId}/task/{taskId}/update")
    suspend fun updateTask(
        @Path("projectId") projectId: Long,
        @Path("sectionId") sectionId: Long,
        @Path("taskId") taskId: Long,
        @Body request: UpdateTaskRequestDto
    ): Response<TaskResponseDto>

    @PATCH("projects/{projectId}/section/{sectionId}/task/{taskId}/complete")
    suspend fun completeTask(
        @Path("projectId") projectId: Long,
        @Path("sectionId") sectionId: Long,
        @Path("taskId") taskId: Long,
        @Body request: CompleteTaskRequestDto
    ): Response<TaskResponseDto>

    @DELETE("projects/{projectId}/section/{sectionId}/task/{taskId}/delete")
    suspend fun deleteTask(
        @Path("projectId") projectId: Long,
        @Path("sectionId") sectionId: Long,
        @Path("taskId") taskId: Long
    ): Response<Unit>
}
