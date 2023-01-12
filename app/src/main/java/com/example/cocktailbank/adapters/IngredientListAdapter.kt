package com.example.cocktailbank.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktailbank.databinding.IngredientListRowLayoutBinding
import com.example.cocktailbank.models.CocktailsRecipe
import com.example.cocktailbank.models.Drink
import com.example.cocktailbank.util.CocktailRecipesDiffUtil

class IngredientListAdapter : RecyclerView.Adapter<IngredientListAdapter.MyViewHolder>() {

    private var ingredients = emptyList<Drink>()

    class MyViewHolder(val binding: IngredientListRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredientName: String) {
            binding.ingredientName = ingredientName
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IngredientListRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentIngredient = ingredients[position]
        holder.bind(currentIngredient.strIngredient1!!)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setData(newData: CocktailsRecipe) {
        val ingredientListDiffUtil = newData.drinks?.let { CocktailRecipesDiffUtil(ingredients, it) }
        val diffUtilResult = ingredientListDiffUtil?.let { DiffUtil.calculateDiff(it) }
        ingredients = newData.drinks!!.sortedBy { it.strIngredient1 }
        diffUtilResult?.dispatchUpdatesTo(this)
    }
}