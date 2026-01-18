package com.example.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quiz.ui.screen.QuestionScreen
import com.example.quiz.ui.screen.StartScreen
import com.example.quiz.ui.screen.TheEndScreen
import com.example.quiz.ui.viewmodel.QuizViewModel
import com.example.quiz.ui.viewmodel.State

@Composable
fun App() {
    val holder: QuizViewModel = viewModel()

    Scaffold() { padding -> 
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (holder.uiState.state) {
                State.START -> StartScreen(holder::onStartQuiz)
                State.QUIZ -> QuestionScreen(
                    holder.uiState,
                    holder.currQuestion,
                    holder.questionsSize,
                    holder::onSelectAnswer,
                    holder::onSubmitAnswer
                )
                State.THE_END -> TheEndScreen(
                    holder.uiState.trueAnswers,
                    holder.questionsSize,
                    holder.result,
                    holder.text,
                    holder::onStartQuiz
                )
            }
        }
    }
}
