package com.example.twdist_android.features.favorite.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twdist_android.features.favorite.presentation.components.FavoriteProjectList
import com.example.twdist_android.features.favorite.presentation.model.FavoriteProjectState

@Composable
fun FavoriteProjectScreen(
    favoriteProjects: List<FavoriteProjectState>
) {
    Column {
        Row(modifier = Modifier.padding(vertical = 5.dp)) {
            Text(text = "Name", modifier = Modifier.padding(start = 39.dp), fontSize = 15.sp)
            Spacer(Modifier.weight(1f))
            Text(text = "Pending Tasks", modifier = Modifier.padding(end = 15.dp), fontSize = 15.sp)
        }
        FavoriteProjectList(favoriteProjects)
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteProjectScreenPreview() {
    val sampleProjects = listOf(
        FavoriteProjectState("Web App Development", 5),
        FavoriteProjectState("Movile App Development", 2),
        FavoriteProjectState("Final Course Project", 8)
    )
    FavoriteProjectScreen(favoriteProjects = sampleProjects)
}