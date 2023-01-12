package com.example.cocktailbank.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktailbank.models.CocktailsRecipe
import com.example.cocktailbank.models.Drink
import com.example.cocktailbank.models.Ingredient
import com.example.cocktailbank.ui.fragment.IngredientList.IngredientListFragmentDirections
import com.example.cocktailbank.util.NetworkResult

class IngredientListBinding {
    companion object {

        @BindingAdapter("readIngredientListApiResponse")
        @JvmStatic
        fun setRecyclerViewAndProgressVisibility(
            view: View,
            apiResponse: NetworkResult<CocktailsRecipe>
        ) {
            when (apiResponse) {
                is NetworkResult.Success -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is RecyclerView -> {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
                is NetworkResult.Error -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is RecyclerView -> {
                            view.visibility = View.INVISIBLE
                        }
                        is ImageView -> {
                            view.visibility = View.VISIBLE
                        }
                        is TextView -> {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> {
                            view.visibility = View.VISIBLE
                        }
                        is RecyclerView -> {
                            view.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }

        @BindingAdapter("onIngredientClickListener")
        @JvmStatic
        fun onIngredientClickListener(
            ingredientListRowLayout: ConstraintLayout,
            ingredientName: String
        ) {
            ingredientListRowLayout.setOnClickListener {

                try {
                    val action =
                        IngredientListFragmentDirections.actionIngredientListFragmentToIngredientInfoActivity(
                            ingredientName
                        )
                    ingredientListRowLayout.findNavController().navigate(action)
                } catch (e: Exception) {
                    Log.d("onIngredientClickListener", e.message.toString())
                }

            }
        }

    }
}