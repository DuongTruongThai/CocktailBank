package com.example.cocktailbank.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktailbank.databinding.IngredientsRowLayoutBinding
import com.example.cocktailbank.util.CocktailRecipesDiffUtil

class CocktailIngredientsAdapter : RecyclerView.Adapter<CocktailIngredientsAdapter.MyViewHolder>() {

    private var ingredientList = emptyList<String>()
    private var amountList = emptyList<String>()

    class MyViewHolder(private val binding: IngredientsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: String, amount: String) {
            binding.ingredientNameTextView.text = ingredient
            binding.ingredientAmountTextView.text = amount
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = IngredientsRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentIngredient = ingredientList[position]
        if (amountList.size > position){
            val currentAmount = amountList[position]
            holder.bind(currentIngredient, currentAmount)
        }
        else {
            holder.bind(currentIngredient, "")
        }

    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    fun setData(newIngredientList: List<String>, newAmountList: List<String>) {
        val ingredientListDiffUtil = CocktailRecipesDiffUtil(ingredientList, newIngredientList)
        val amountListDiffUtil = CocktailRecipesDiffUtil(amountList, newAmountList)

        val ingredientDiffUtilResult = DiffUtil.calculateDiff(ingredientListDiffUtil)
        val amountDiffUtilResult = DiffUtil.calculateDiff(amountListDiffUtil)

        ingredientList = newIngredientList
        amountList = newAmountList
        ingredientDiffUtilResult.dispatchUpdatesTo(this)
        amountDiffUtilResult.dispatchUpdatesTo(this)
    }
}