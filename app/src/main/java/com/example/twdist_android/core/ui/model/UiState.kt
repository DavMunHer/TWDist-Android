package com.example.twdist_android.core.ui.model

// Please note that this is only an example for handling generic ui state.
// This allows to control loaders (when request is still loading), errors and success, most of this would make sense when doing http req
sealed interface UiState<out T> {

    object Loading : UiState<Nothing>

    data class Success<T>(
        val data: T
    ) : UiState<T>

    data class Error(
        val message: String
    ) : UiState<Nothing>

    object Empty : UiState<Nothing>
}

//Example of usage:

/*
@Composable
fun ProjectListScreen(
    uiState: UiState<List<Project>>,
    onRetry: () -> Unit
) {
    when (uiState) {
        is UiState.Loading -> {
            CircularProgressIndicator()
        }

        is UiState.Success -> {
            ProjectList(projects = uiState.data)
        }

        is UiState.Empty -> {
            Text("No projects available")
        }

        is UiState.Error -> {
            Column {
                Text(text = uiState.message)
                Button(onClick = onRetry) {
                    Text("Try again")
                }
            }
        }
    }
}

For previews:
@Preview(showBackground = true)
@Composable
fun ProjectListScreenSuccessPreview() {
    ProjectListScreen(
        uiState = UiState.Success(
            data = listOf(
                Project(id = "1", name = "Project A"),
                Project(id = "2", name = "Project B")
            )
        ),
        onRetry = {}
    )
}
 */