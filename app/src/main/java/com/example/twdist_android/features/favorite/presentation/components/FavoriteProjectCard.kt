package com.example.twdist_android.features.favorite.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twdist_android.features.favorite.presentation.model.FavoriteProjectState

@Composable
fun FavoriteProjectCard(project: FavoriteProjectState) {
    Card(shape = RoundedCornerShape(0.dp), modifier = Modifier.background(Color.White)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            // FIXME The icon should be a # not an arrow
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Tag")
            Spacer(Modifier.padding(5.dp))
            Text(text = project.name, fontSize = 20.sp)
            Spacer(Modifier.weight(1f))
            Text(
                modifier = Modifier.padding(end = 10.dp),
                text = "${project.pendingTasks}",
                fontSize = 20.sp
            )

        }
    }
}

@Preview
@Composable
fun PreviewFavoriteProjectCard() {
    FavoriteProjectCard(FavoriteProjectState(name = "English Academy", pendingTasks = 2))
}