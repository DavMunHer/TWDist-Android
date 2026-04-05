package com.example.twdist_android.features.projectdetails.presentation.model

data class ProjectDetailsUiState(
    val isLoading: Boolean = true,
    val project: ProjectDetailsUi? = null,
    val error: String? = null
)

data class ProjectDetailsUi(
    val id: Long,
    val name: String,
    val isFavorite: Boolean,
    val sections: List<SectionUi>
)

data class SectionUi(
    val name: String,
    val tasks: List<String>
)
