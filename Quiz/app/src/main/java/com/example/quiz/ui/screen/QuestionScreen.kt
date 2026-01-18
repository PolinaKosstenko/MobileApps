package com.example.quiz.ui.screen

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiz.model.Question
import com.example.quiz.ui.viewmodel.QuizUIState

@Composable
fun QuestionScreen(
    uiState: QuizUIState,
    currentQuestion: Question,
    questionsSize: Int,
    onSelectAnswer: (Int) -> Unit,
    onSubmitAnswer: () -> Unit
) {
    Card(
        modifier = Modifier.width((LocalConfiguration.current.screenWidthDp * 0.7).dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text =
                    """
                    Вопрос ${currentQuestion.id} из ${questionsSize}
                    ${currentQuestion.question}
                """.trimIndent(),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            for(i in 0..currentQuestion.answers.size - 1) {
                Row(
                    Modifier
                        .selectable(
                            selected = (i == uiState.answer),
                            onClick = { onSelectAnswer(i) },
                            role = Role.RadioButton,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        )
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = (i == uiState.answer),
                        onClick = null
                    )
                    Text(
                        text = currentQuestion.answers[i],
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Button(
                enabled = (uiState.answer != -1),
                onClick = onSubmitAnswer,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Далее")
            }
        }
    }
}
