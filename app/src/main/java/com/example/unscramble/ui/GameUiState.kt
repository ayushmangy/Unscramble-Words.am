package com.example.unscramble.ui

data class GameUiState(
    val currScrambledWord : String = "",
    val isGuessWrong: Boolean= false,
    val score: Int = 0,
    val currWordCount: Int = 1,
    val isGameOver: Boolean = false

)
