package com.example.parcialcarrascof

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.parcialcarrascof.databinding.FragmentAnswerBinding

class AnswerFragment : Fragment() {

    private var _binding: FragmentAnswerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAnswerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentQuestionIndex = viewModel.currentQuestionIndex.value ?: 0
        val questions = (parentFragmentManager.fragments.firstOrNull { it is QuestionFragment }
                as? QuestionFragment)?.questions ?: emptyList()

        if (currentQuestionIndex <= 0 || currentQuestionIndex > questions.size) {
            binding.resultText.text = "No hay más preguntas disponibles."
            return
        }

        val currentQuestion = questions[currentQuestionIndex - 1]

        binding.resultText.text = if ((viewModel.score.value ?: 0) > currentQuestionIndex)
            "¡Correcto!" else "Incorrecto"
        binding.correctAnswerText.text =
            "Respuesta correcta: ${currentQuestion.options[currentQuestion.correctAnswerIndex]}"
        binding.explanationText.text = getExplanation(currentQuestionIndex)
    }

    private fun getExplanation(questionIndex: Int): String {
        return when (questionIndex) {
            0 -> "El Real Madrid ha ganado la Champions League en 14 ocasiones, más que cualquier otro equipo."
            1 -> "Cristiano Ronaldo es el máximo goleador de la Champions League con más de 130 goles."
            2 -> "Manchester United es conocido como 'The Red Devils' (Los Diablos Rojos)."
            3 -> "Borussia Dortmund es un club alemán que juega en la Bundesliga."
            4 -> "Diego Maradona era conocido como 'El Pibe de Oro' por su habilidad excepcional."
            5 -> "La Juventus es conocida como 'La Vecchia Signora' (La Vieja Señora) en Italia."
            6 -> "El Camp Nou es el estadio del FC Barcelona, uno de los más grandes de Europa."
            7 -> "Manchester United ha ganado la Premier League en 13 ocasiones, más que cualquier otro equipo."
            8 -> "La Eurocopa 2020 se celebró en 2021 debido a la pandemia, y los partidos se jugaron en múltiples países."
            9 -> "Karim Benzema ganó el Balón de Oro en 2022 por su excepcional desempeño con el Real Madrid."
            else -> "No hay explicación disponible para esta pregunta."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}