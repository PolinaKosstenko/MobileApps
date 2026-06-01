package com.example.harrypotterapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harrypotterapi.model.House
import com.example.harrypotterapi.model.Question
import com.example.harrypotterapi.model.sortingQuestions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuizViewModel(val questions: List<Question> = sortingQuestions) : ViewModel() {
    private val currentQuestionIndexFlow = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = currentQuestionIndexFlow

    private val answersFlow = MutableStateFlow<List<Map<String, Int>>>(emptyList())
    val answers: StateFlow<List<Map<String, Int>>> = answersFlow

    fun addAnswer(answerPoints: Map<String, Int>) {
        answersFlow.update { answers -> answers + answerPoints }
        if (currentQuestionIndexFlow.value < 4) {
            currentQuestionIndexFlow.value++
        }
    }

    fun calculateFaculty(): String {
        val totalPoints = mutableMapOf(
            "gryffindor" to 0,
            "slytherin" to 0,
            "ravenclaw" to 0,
            "hufflepuff" to 0
        )

        answersFlow.value.forEach { answerPoints ->
            answerPoints.forEach { (faculty, points) ->
                totalPoints[faculty] = totalPoints.getOrDefault(faculty, 0) + points
            }
        }

        return totalPoints.maxByOrNull { it.value }?.key ?: "hufflepuff"
    }

    fun onDecideHouse(): House {
        currentQuestionIndexFlow.update { 0 }
        return when (calculateFaculty()) {
            "gryffindor" -> House.Gryffindor
            "slytherin" -> House.Slytherin
            "ravenclaw" -> House.Ravenclaw
            "hufflepuff" -> House.Hufflepuff
            else -> House.NoHouse
        }
    }
}
