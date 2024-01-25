package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.words
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameVM : ViewModel() {
    var userGuess by mutableStateOf("")
        private set

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private lateinit var currword: String
    private var usedWords: MutableSet<String> = mutableSetOf()

    private fun pickRandWordAndShuffle(): String {
        currword = words.random()
        return if (usedWords.contains(currword)) {
            pickRandWordAndShuffle()
        } else {
            usedWords.add(currword)
            shuffleRandWord(currword)
        }
    }

    private fun shuffleRandWord(word: String): String {
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currScrambledWord = pickRandWordAndShuffle())
    }

    init {
        resetGame()
    }

    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    fun checkUserGuess() {
        if (userGuess.equals(currword, ignoreCase = true)) {
            val updatedScore = _uiState.value.score + SCORE_INCREASE
            updatedGameState(updatedScore)
        } else {
            _uiState.update { currentState -> currentState.copy(isGuessWrong = true) }
        }
        updateUserGuess("")
    }

    private fun updatedGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessWrong = false,
                    currScrambledWord = pickRandWordAndShuffle(),
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessWrong = false,
                    currScrambledWord = pickRandWordAndShuffle(),
                    score = updatedScore,
                    currWordCount = currentState.currWordCount.inc()
                )
            }
        }
    }

    fun skipWord() {
        updatedGameState(_uiState.value.score)
        updateUserGuess("")
    }
}
