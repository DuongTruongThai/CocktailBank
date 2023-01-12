package com.example.cocktailbank.ui.fragment.CocktailOverview

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import coil.load
import com.example.cocktailbank.databinding.FragmentCocktailOverviewBinding
import com.example.cocktailbank.models.Drink
import com.example.cocktailbank.util.Constants.Companion.BUNDLE_KEY_1
import com.example.cocktailbank.util.Constants.Companion.BUNDLE_KEY_2
import com.example.cocktailbank.util.NetworkResult
import com.example.cocktailbank.viewmodels.MainViewModel
import com.example.cocktailbank.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CocktailOverviewFragment : Fragment() {

    private var _binding: FragmentCocktailOverviewBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private val recipesViewModel: RecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCocktailOverviewBinding.inflate(layoutInflater)

        val args = arguments
        val myBundle: Drink = args!!.getParcelable(BUNDLE_KEY_1, Drink::class.java) as Drink

        binding.detailMainImageView.load(myBundle.strDrinkThumb)
        binding.detailDrinkTitle.text = myBundle.strDrink
        binding.detailCategoryTextView.text = myBundle.strCategory ?: "..."
        binding.detailAlcoholicTypeTextView.text = myBundle.strAlcoholic ?: "..."
        binding.detailGlassTextView.text = myBundle.strGlass ?: "..."
        binding.detailInstructionTextView.text = myBundle.strInstructions ?: "..."
        //requestApiDataById(myBundle, args)

        return binding.root
    }

    private fun requestApiDataById(drink: Drink, args: Bundle) {
        mainViewModel.getRecipeById(recipesViewModel.applyQueriesId(drink.idDrink))
        mainViewModel.recipesResponseById.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        binding.detailCategoryTextView.text = it.drinks?.get(0)?.strCategory ?: "..."
                        binding.detailAlcoholicTypeTextView.text = it.drinks?.get(0)?.strAlcoholic ?: "..."
                        binding.detailGlassTextView.text = it.drinks?.get(0)?.strGlass ?: "..."
                        binding.detailInstructionTextView.text = it.drinks?.get(0)?.strInstructions ?: "..."
                        args.putParcelable(BUNDLE_KEY_2, it.drinks!![0])
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}