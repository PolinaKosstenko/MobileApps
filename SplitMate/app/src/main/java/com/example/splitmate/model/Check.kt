package com.example.splitmate.model

import java.time.LocalDate


data class Check(
    val id: Int,
    val total: Int,
    val people: Int,
    val tip: Int,
    val date: LocalDate
)
