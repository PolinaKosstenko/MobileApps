package com.example.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.quiz.data.Questions
import com.example.quiz.ui.screen.QuestionScreen
import com.example.quiz.ui.screen.StartScreen
import com.example.quiz.ui.screen.TheEndScreen

enum class State {
    START,
    QUIZ,
    THE_END
}

@Composable
fun App() {
    var state: State by remember { mutableStateOf(State.START) }
    var trueAnswers: Int by remember { mutableStateOf(0) }
    val startQuiz: () -> Unit = { state = State.QUIZ; trueAnswers = 0 }

    Scaffold() { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                State.START -> StartScreen(startQuiz)
                State.QUIZ -> QuestionScreen(Questions, { state = State.THE_END }) {
                    ++trueAnswers
                }
                State.THE_END -> TheEndScreen(trueAnswers, Questions.size, startQuiz)
            }
        }
    }
}
