package com.example.twdist_android.features.today.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun TodayEmptyScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "https://img.freepik.com/vector-premium/lindo-panda-esta-sentado-espalda-icono-plano-simple-estilo-retro_651154-3715.jpg",
            contentDescription = "Panda",
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = "¿Qué debes completar hoy?",
            fontSize = 25.sp,
            fontWeight = FontWeight.W300,
            modifier = Modifier.padding(15.dp)
        )
        Text(
            text = "Las tareas que añadas aquí se asignan para el día de hoy.",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 12.sp
        )
    }
}

@Preview
@Composable
fun TodayEmptyScreenPreview() {
    TodayEmptyScreen()
}