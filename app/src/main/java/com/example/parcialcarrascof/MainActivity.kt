/**
 * Juego de preguntas y respuestas.
 * Autor: Fabricio Gabriel Carrazco Arana
 * Fecha de creación: 17/10/2024
 * Última modificación: 19/10/2024
 */

package com.example.parcialcarrascof

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.parcialcarrascof.databinding.ActivityMainBinding

class MainActivity : FragmentActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.welcomeFragment -> binding.progressBar.visibility = View.GONE
                else -> binding.progressBar.visibility = View.VISIBLE
            }
        }

        viewModel.currentQuestionIndex.observe(this) { index ->
            binding.progressBar.progress = ((index + 1) * 100) / 10
        }
    }
}