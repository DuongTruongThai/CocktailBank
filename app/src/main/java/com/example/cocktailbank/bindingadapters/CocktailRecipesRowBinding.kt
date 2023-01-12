package com.example.cocktailbank.bindingadapters

import android.util.Log
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.example.cocktailbank.R
import com.example.cocktailbank.models.Drink
import com.example.cocktailbank.ui.fragment.Cocktails.CocktailsFragmentDirections

class CocktailRecipesRowBinding {
    companion object {

        @BindingAdapter("onCocktailClickListener")
        @JvmStatic
        fun onCocktailClickListener(cocktailRowLayout: ConstraintLayout, drink: Drink) {
            cocktailRowLayout.setOnClickListener {
                try {
                    val action = CocktailsFragmentDirections.actionCocktailsFragmentToDetailActivity(drink)
                    cocktailRowLayout.findNavController().navigate(action)
                }catch (e: Exception) {
                    Log.d("onCocktailClickListener", e.message.toString())
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }
    }
}