package com.example.quiz.model

data class Question(
    var id: Int,
    var question: String,
    var answers: List<String>,
    var trueAnswer: Int
)
