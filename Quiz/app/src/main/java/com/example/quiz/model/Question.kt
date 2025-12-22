package com.example.quiz.model

data class Question(
    val id: Int,
    val question: String,
    val answers: List<String>,
    val trueAnswer: Int
)
