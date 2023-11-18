package com.example.cocktailbank.ui.fragment.IngredientList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocktailbank.adapters.IngredientListAdapter
import com.example.cocktailbank.databinding.FragmentIngredientListBinding
import com.example.cocktailbank.util.NetworkResult
import com.example.cocktailbank.viewmodels.MainViewModel
import com.example.cocktailbank.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IngredientListFragment : Fragment() {

    private var _binding: FragmentIngredientListBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val recipesViewModel: RecipesViewModel by viewModels()
    private val mAdapter: IngredientListAdapter by lazy { IngredientListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientListBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel

        setupRecyclerView()
        requestApiIngredientList()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.ingredientListRecyclerView.adapter = mAdapter
        binding.ingredientListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun requestApiIngredientList() {
        mainViewModel.getIngredientList(recipesViewModel.applyQueriesIngredientList())
        mainViewModel.ingredientListResponse.observe(viewLifecycleOwner) { response ->
            when(response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        mAdapter.setData(it)
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