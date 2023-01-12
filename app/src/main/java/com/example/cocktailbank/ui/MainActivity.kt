package com.example.cocktailbank.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cocktailbank.R
import com.example.cocktailbank.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id)
        val navController = navHostFragment?.findNavController()
        navController?.let {
            binding.bottomNavigationView.setupWithNavController(it)
            it.addOnDestinationChangedListener { controller, destination, argument ->
                title = destination.label
            }
        }
    }
}