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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quiz.model.Question

@Composable
fun QuestionScreen(questions: List<Question>, theEnd: () -> Unit, cnt: () -> Unit) {
    var number: Int by remember { mutableStateOf(0) }
    var answer: Int by remember { mutableStateOf(-1) }
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
                    Вопрос ${questions[number].id} из ${questions.size}
                    ${questions[number].question}
                """.trimIndent(),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            for(i in 0..questions[number].answers.size - 1) {
                Row(
                    Modifier
                        .selectable(
                            selected = (i == answer),
                            onClick = { answer = i },
                            role = Role.RadioButton,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        )
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = (i == answer),
                        onClick = null
                    )
                    Text(
                        text = questions[number].answers[i],
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Button(
                enabled = (answer != -1),
                onClick = {
                    if (answer == questions[number].trueAnswer) cnt()
                    if (number < questions.size - 1) number += 1
                    else theEnd()
                    answer = -1
                },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Далее")
            }
        }
    }
}
