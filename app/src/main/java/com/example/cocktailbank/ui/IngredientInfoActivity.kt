package com.example.cocktailbank.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.navArgs
import com.example.cocktailbank.R
import com.example.cocktailbank.databinding.ActivityIngredientInfoBinding
import com.example.cocktailbank.util.NetworkResult
import com.example.cocktailbank.viewmodels.MainViewModel
import com.example.cocktailbank.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngredientInfoActivity : AppCompatActivity() {

    private val binding: ActivityIngredientInfoBinding by lazy {
        ActivityIngredientInfoBinding.inflate(
            layoutInflater
        )
    }
    private val args by navArgs<IngredientInfoActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private val recipesViewModel: RecipesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.ingredientInforToolbar)
        binding.ingredientInforToolbar.setTitleTextColor(
            ContextCompat.getColor(
                this,
                R.color.white
            )
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        requestIngredientInforApiData(args.ingredientName)
    }

    private fun requestIngredientInforApiData(ingredientName: String) {
        mainViewModel.searchIngredient(recipesViewModel.applySearchIngredientQuery(ingredientName))
        mainViewModel.searchIngredientResponse.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    binding.ingredientInforProgressBar.isVisible = false
                    binding.ingredientInfoScrollView.isVisible = true
                    response.data?.let {
                        binding.ingredientInfoNameTextView.text =
                            it.ingredients[0].strIngredient ?: "..."
                        binding.ingredientInfoTypeTextView.text = it.ingredients[0].strType ?: "..."
                        binding.ingredientInfoAlcoholTextView.text =
                            it.ingredients[0].strAlcohol ?: "..."
                        binding.ingredientInfoDescriptionTextView.text =
                            it.ingredients[0].strDescription ?: "..."
                    }
                }
                is NetworkResult.Error -> {
                    binding.ingredientInforProgressBar.isVisible = false
                    Toast.makeText(
                        this,
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    binding.ingredientInforProgressBar.isVisible = true
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}