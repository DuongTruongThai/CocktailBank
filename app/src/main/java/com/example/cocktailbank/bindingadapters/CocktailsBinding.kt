package com.example.cocktailbank.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.example.cocktailbank.data.database.entities.CocktailsEntity
import com.example.cocktailbank.models.CocktailsRecipe
import com.example.cocktailbank.util.NetworkResult

class CocktailsBinding {

    companion object {

        @BindingAdapter("readApiResponse", "readDatabase", requireAll = true)
        @JvmStatic
        fun handleReadDataError(
            view: View,
            apiResponse: NetworkResult<CocktailsRecipe>?,
            database:List<CocktailsEntity>?
        ) {
            when (view) {
                is ImageView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                }
                is TextView -> {
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                    view.text = apiResponse?.message.toString()
                }
            }
        }

    }

}