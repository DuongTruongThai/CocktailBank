package com.example.cocktailbank.ui.fragment.CocktailIngredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocktailbank.R
import com.example.cocktailbank.adapters.CocktailIngredientsAdapter
import com.example.cocktailbank.databinding.FragmentCocktailIngredientsBinding
import com.example.cocktailbank.models.Drink
import com.example.cocktailbank.util.Constants
import com.example.cocktailbank.util.Constants.Companion.BUNDLE_KEY_1
import com.example.cocktailbank.util.Constants.Companion.BUNDLE_KEY_2

class CocktailIngredientsFragment : Fragment() {

    private var _binding: FragmentCocktailIngredientsBinding? = null
    private val binding get() = _binding!!

    private val mAdapter: CocktailIngredientsAdapter by lazy { CocktailIngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCocktailIngredientsBinding.inflate(layoutInflater)

        val args = arguments
        val myBundle: Drink = args!!.getParcelable(BUNDLE_KEY_1, Drink::class.java) as Drink

        setupRecyclerView()
        myBundle.let {
            mAdapter.setData(getIngredientList(it), getAmountList(it))
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.detailIngredientsRecyclerView.adapter = mAdapter
        binding.detailIngredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getIngredientList(drink: Drink): List<String> {
        val ingredientList: MutableList<String> = mutableListOf()
        drink.strIngredient1?.let { ingredientList.add(it) }
        drink.strIngredient2?.let { ingredientList.add(it) }
        drink.strIngredient3?.let { ingredientList.add(it) }
        drink.strIngredient4?.let { ingredientList.add(it) }
        drink.strIngredient5?.let { ingredientList.add(it) }
        drink.strIngredient6?.let { ingredientList.add(it) }
        drink.strIngredient7?.let { ingredientList.add(it) }
        drink.strIngredient8?.let { ingredientList.add(it) }
        drink.strIngredient9?.let { ingredientList.add(it) }
        drink.strIngredient10?.let { ingredientList.add(it) }
        drink.strIngredient11?.let { ingredientList.add(it) }
        drink.strIngredient12?.let { ingredientList.add(it) }
        drink.strIngredient13?.let { ingredientList.add(it) }
        drink.strIngredient14?.let { ingredientList.add(it) }
        drink.strIngredient15?.let { ingredientList.add(it) }

        return ingredientList
    }

    private fun getAmountList(drink: Drink): List<String> {
        val amountList: MutableList<String> = mutableListOf()
        drink.strMeasure1?.let { amountList.add(it) }
        drink.strMeasure2?.let { amountList.add(it) }
        drink.strMeasure3?.let { amountList.add(it) }
        drink.strMeasure4?.let { amountList.add(it) }
        drink.strMeasure5?.let { amountList.add(it) }
        drink.strMeasure6?.let { amountList.add(it) }
        drink.strMeasure7?.let { amountList.add(it) }
        drink.strMeasure8?.let { amountList.add(it) }
        drink.strMeasure9?.let { amountList.add(it) }
        drink.strMeasure10?.let { amountList.add(it) }
        drink.strMeasure11?.let { amountList.add(it) }
        drink.strMeasure12?.let { amountList.add(it) }
        drink.strMeasure13?.let { amountList.add(it) }
        drink.strMeasure14?.let { amountList.add(it) }
        drink.strMeasure15?.let { amountList.add(it) }

        return amountList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}