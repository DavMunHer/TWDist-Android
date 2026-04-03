package com.example.twdist_android.features.projectdetails.presentation.model

data class ProjectDetailsUi(
    val id: Long,
    val name: String,
    val description: String,
    val sections: List<String>
)

object ProjectDetailsMockData {
    private val projects = listOf(
        ProjectDetailsUi(
            id = 1L,
            name = "Mock Project Alpha",
            description = "Placeholder details for Project Alpha.",
            sections = listOf("Backlog", "In Progress", "Done")
        ),
        ProjectDetailsUi(
            id = 2L,
            name = "Mock Project Beta",
            description = "Placeholder details for Project Beta.",
            sections = listOf("Ideas", "Planned")
        ),
        ProjectDetailsUi(
            id = 3L,
            name = "Mock Project Gamma",
            description = "Placeholder details for Project Gamma.",
            sections = listOf("Open", "Review", "Completed")
        )
    )

    fun findById(projectId: Long): ProjectDetailsUi? {
        return projects.firstOrNull { it.id == projectId }
    }
}
