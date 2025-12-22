package com.example.quiz.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TheEndScreen(trueAnswers: Int, size: Int, startQuiz: () -> Unit) {
    val result: Int = (trueAnswers.toDouble() / size * 100).toInt()

    val text: String = when (result) {
        in 0..50 -> "Вы Златопуст Локанс, как жаль"
        in 51..80 -> "Вы студент Хогвартса, но до Гермионы Грейнджер не дотягиваете"
        in 81..100 -> "Пора сразиться с Воландемортом в финальной битве"

        else -> ""
    }

    Column(
        Modifier.width((LocalConfiguration.current.screenWidthDp * 0.7).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Результаты",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Text(
            text ="Вы ответили правильно на ${trueAnswers} из ${size}\n(${result}%)",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 5.dp)
        )


        Text(
            text = text,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(bottom = 15.dp)
        )

        Button(onClick = startQuiz) {
            Text(
                text = "Пройти еще раз",
                textAlign = TextAlign.Center
            )
        }
    }
}
