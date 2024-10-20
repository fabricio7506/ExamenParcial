package com.example.parcialcarrascof

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.parcialcarrascof.databinding.FragmentQuestionBinding

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()

    val questions = listOf(
        Question(
            "¿Qué equipo ha ganado más Champions League?",
            listOf("Real Madrid", "Barcelona", "Bayern Munich", "Liverpool"), 0
        ),
        Question(
            "¿Quién es el máximo goleador histórico de la Champions League?",
            listOf("Lionel Messi", "Cristiano Ronaldo", "Raúl González", "Robert Lewandowski"), 1
        ),
        Question(
            "¿Qué club inglés es conocido como 'The Red Devils'?",
            listOf("Liverpool", "Arsenal", "Manchester United", "Chelsea"), 2
        ),
        Question(
            "¿En qué país juega el club Borussia Dortmund?",
            listOf("Francia", "España", "Italia", "Alemania"), 3
        ),
        Question(
            "¿Qué jugador es conocido como 'El Pibe de Oro'?",
            listOf("Pelé", "Diego Maradona", "Zinedine Zidane", "Johan Cruyff"), 1
        ),
        Question(
            "¿Cómo se conoce a la Juventus en Italia?",
            listOf("La Vecchia Signora", "El Viejo Maestro", "La Vieja Dama", "El Gigante Italiano"), 0
        ),
        Question(
            "¿Cuál es el estadio del FC Barcelona?",
            listOf("Santiago Bernabéu", "Allianz Arena", "Camp Nou", "San Mamés"), 2
        ),
        Question(
            "¿Cuántas veces ha ganado Manchester United la Premier League?",
            listOf("10", "15", "20", "13"), 3
        ),
        Question(
            "¿Por qué se celebró la Eurocopa 2020 en 2021?",
            listOf("Por el clima", "Por la pandemia", "Por la falta de equipos", "Por la UEFA"), 1
        ),
        Question(
            "¿Quién ganó el Balón de Oro en 2022?",
            listOf("Lionel Messi", "Cristiano Ronaldo", "Karim Benzema", "Robert Lewandowski"), 2
        )
    )

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handler = Handler(Looper.getMainLooper())

        // Inicia el temporizador que disminuye cada segundo
        runnable = object : Runnable {
            override fun run() {
                viewModel.decrementTimer()
                if (viewModel.timeRemaining.value!! > 0) {
                    handler.postDelayed(this, 1000)
                }
            }
        }
        handler.post(runnable)

        // Observa los cambios en el índice de la pregunta
        viewModel.currentQuestionIndex.observe(viewLifecycleOwner) { index ->
            if (index < questions.size) {
                displayQuestion(questions[index])
            } else {
                findNavController().navigate(R.id.action_questionFragment_to_resultFragment)
            }
        }

        // Observa los cambios en el temporizador
        viewModel.timeRemaining.observe(viewLifecycleOwner) { time ->
            binding.timerText.text = "Tiempo: $time s"
            if (time == 0) {
                Toast.makeText(requireContext(), "Tiempo agotado", Toast.LENGTH_SHORT).show()
                viewModel.answerQuestion(false)  // Marca como incorrecta por tiempo agotado
            }
        }

        // Manejo del botón de envío de respuesta
        binding.submitButton.setOnClickListener {
            val selectedOptionId = binding.answerRadioGroup.checkedRadioButtonId

            if (selectedOptionId == -1) {
                Toast.makeText(
                    requireContext(), "Por favor selecciona una opción.", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val selectedAnswerIndex = when (selectedOptionId) {
                R.id.option1 -> 0
                R.id.option2 -> 1
                R.id.option3 -> 2
                R.id.option4 -> 3
                else -> -1
            }

            val currentQuestionIndex = viewModel.currentQuestionIndex.value!!
            val isCorrect = selectedAnswerIndex == questions[currentQuestionIndex].correctAnswerIndex

            viewModel.answerQuestion(isCorrect)
        }
    }

    private fun displayQuestion(question: Question) {
        binding.questionText.text = question.text
        binding.option1.text = question.options[0]
        binding.option2.text = question.options[1]
        binding.option3.text = question.options[2]
        binding.option4.text = question.options[3]
        binding.answerRadioGroup.clearCheck()
        viewModel.resetTimer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacks(runnable)  // Detenemos el temporizador cuando se destruye la vista
    }
}

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)
