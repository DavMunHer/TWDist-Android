package com.example.twdist_android.features.upcoming.data.remote

import com.example.twdist_android.features.projectdetails.data.dto.task.CompleteTaskRequestDto
import com.example.twdist_android.features.upcoming.data.dto.UpcomingTaskResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface UpcomingApi {
    @GET("tasks/upcoming")
    suspend fun getUpcomingTasks(
        @Query("from") from: String,
        @Query("to") to: String
    ): Response<List<UpcomingTaskResponseDto>>

    @PATCH("projects/{projectId}/section/{sectionId}/task/{taskId}/complete")
    suspend fun completeTask(
        @Path("projectId") projectId: Long,
        @Path("sectionId") sectionId: Long,
        @Path("taskId") taskId: Long,
        @Body request: CompleteTaskRequestDto
    ): Response<Unit>
}
