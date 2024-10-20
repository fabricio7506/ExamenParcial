package com.example.parcialcarrascof

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    private val _timeRemaining = MutableLiveData(30)
    val timeRemaining: LiveData<Int> = _timeRemaining

    // Esta función procesa la respuesta del usuario y avanza a la siguiente pregunta
    fun answerQuestion(isCorrect: Boolean) {
        if (isCorrect) {
            _score.value = _score.value?.plus(1)
        }
        moveToNextQuestion()  // Aislamos la lógica de avance
    }

    // Mueve al siguiente índice y reinicia el temporizador
    private fun moveToNextQuestion() {
        _currentQuestionIndex.value = _currentQuestionIndex.value?.plus(1)
        resetTimer()  // Reinicia el tiempo para la siguiente pregunta
    }

    // Decrementa el temporizador en 1 segundo
    fun decrementTimer() {
        if (_timeRemaining.value ?: 0 > 0) {
            _timeRemaining.value = _timeRemaining.value?.minus(1)
        }
    }

    // Reinicia el temporizador a 30 segundos
    fun resetTimer() {
        _timeRemaining.value = 30
    }

    // Reinicia el juego desde cero
    fun resetGame() {
        _currentQuestionIndex.value = 0
        _score.value = 0
        resetTimer()
    }
}
