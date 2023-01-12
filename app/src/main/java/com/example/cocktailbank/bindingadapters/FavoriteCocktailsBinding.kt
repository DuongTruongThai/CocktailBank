package com.example.cocktailbank.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktailbank.adapters.FavoriteCocktailsAdapter
import com.example.cocktailbank.data.database.entities.FavoriteCocktailsEntity

class FavoriteCocktailsBinding {

    companion object {

        @BindingAdapter("viewVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoriteCocktailsEntities: List<FavoriteCocktailsEntity>?,
            mAdapter: FavoriteCocktailsAdapter?
        ) {
            when (view) {
                is RecyclerView -> {
                    val dataCheck = favoriteCocktailsEntities.isNullOrEmpty()
                    view.isInvisible = dataCheck
                    favoriteCocktailsEntities?.let {
                        mAdapter?.setData(it)
                    }
                }
                else -> {
                    view.isVisible = favoriteCocktailsEntities.isNullOrEmpty()
                }
            }
        }
    }

}