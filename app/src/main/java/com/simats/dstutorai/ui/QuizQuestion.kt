package com.simats.dstutorai.ui

data class QuizQuestion(
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    var selectedIndex: Int = -1
)
