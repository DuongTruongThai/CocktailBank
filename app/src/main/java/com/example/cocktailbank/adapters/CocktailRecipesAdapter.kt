package com.example.cocktailbank.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktailbank.databinding.CocktailsRowLayoutBinding
import com.example.cocktailbank.models.CocktailsRecipe
import com.example.cocktailbank.models.Drink
import com.example.cocktailbank.util.CocktailRecipesDiffUtil

class CocktailRecipesAdapter : RecyclerView.Adapter<CocktailRecipesAdapter.MyViewHolder>() {

    private var drinks = emptyList<Drink>()

    class MyViewHolder(private val binding: CocktailsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(drink: Drink) {
            binding.drink = drink
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CocktailsRowLayoutBinding.inflate(layoutInflater, parent , false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentDrink = drinks[position]
        holder.bind(currentDrink)
    }

    override fun getItemCount(): Int {
        return drinks.size
    }

    fun setData(newData: CocktailsRecipe) {
        val recipesDiffUtil = newData.drinks?.let { CocktailRecipesDiffUtil(drinks, it) }
        val diffUtilResult = recipesDiffUtil?.let { DiffUtil.calculateDiff(it) }
        drinks = newData.drinks!!
        diffUtilResult?.dispatchUpdatesTo(this)
    }
}