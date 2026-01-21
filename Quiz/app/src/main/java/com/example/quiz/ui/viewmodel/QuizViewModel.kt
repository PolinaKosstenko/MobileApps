package com.example.quiz.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.quiz.data.Questions
import com.example.quiz.model.Question

enum class State {
    START,
    QUIZ,
    THE_END
}

data class QuizUIState(
    var state: State = State.START,
    var trueAnswers: Int = 0,
    var questionNumber: Int = 0,
    var answer: Int = -1
)


class QuizViewModel(val questions: List<Question> = Questions): ViewModel() {
    var uiState: QuizUIState by mutableStateOf(QuizUIState())
    val currQuestion: Question
        get() {
            return questions[uiState.questionNumber]
        }
    val questionsSize: Int
        get() {
            return questions.size
        }

    val result: Int
        get() {
            return (uiState.trueAnswers.toDouble() / questionsSize * 100).toInt()
        }

    val text: String
        get() {
            return when (result) {
                in 0..50 -> "Вы Златопуст Локанс, как жаль"
                in 51..80 -> "Вы студент Хогвартса, но до Гермионы Грейнджер не дотягиваете"
                in 81..100 -> "Пора сразиться с Воландемортом в финальной битве"

                else -> ""
            }
        }


    fun onStartQuiz() {
        uiState = uiState.copy(
            state = State.QUIZ,
            trueAnswers = 0,
            questionNumber = 0,
            answer = -1
        )
    }

    fun onSelectAnswer(n: Int) {
        uiState = uiState.copy(
            answer = n
        )
    }

    fun onSubmitAnswer() {
        val isTrue: Boolean = uiState.answer == questions[uiState.questionNumber].trueAnswer
        val nextQuestion: Boolean = uiState.questionNumber < questions.size - 1

        uiState = uiState.copy(
            state = if (nextQuestion) State.QUIZ else State.THE_END,
            trueAnswers =  uiState.trueAnswers + if (isTrue) 1 else 0,
            questionNumber =  uiState.questionNumber + if (nextQuestion) 1 else 0,
            answer = -1
        )
    }

}