package com.example.cocktailbank.ui.fragment.FavoriteCocktails

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cocktailbank.R
import com.example.cocktailbank.adapters.FavoriteCocktailsAdapter
import com.example.cocktailbank.databinding.FragmentFavoriteCocktailsBinding
import com.example.cocktailbank.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteCocktailsFragment : Fragment() {

    private var _binding: FragmentFavoriteCocktailsBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val mAdapter: FavoriteCocktailsAdapter by lazy { FavoriteCocktailsAdapter(requireActivity(), mainViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteCocktailsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.favorite_cocktails_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.delete_all_favorite_cocktails) {
                    mainViewModel.deleteAllFavoriteCocktails()
                    showSnackBar("All cocktails removed!")
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.favoriteCocktailsRecyclerView.adapter = mAdapter
        binding.favoriteCocktailsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Ok"){}
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mAdapter.clearContextualActionMode()
        _binding = null
    }
}