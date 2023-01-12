package com.example.cocktailbank.ui.fragment.Cocktails.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.cocktailbank.databinding.CocktailsBottomSheetBinding
import com.example.cocktailbank.util.Constants.Companion.ALCOHOLIC_TYPE_FILTER
import com.example.cocktailbank.util.Constants.Companion.CATEGORY_FILTER
import com.example.cocktailbank.util.Constants.Companion.DEFAULT_FILTER_NAME
import com.example.cocktailbank.util.Constants.Companion.DEFAULT_FILTER_TYPE
import com.example.cocktailbank.util.Constants.Companion.GLASS_FILTER
import com.example.cocktailbank.util.Constants.Companion.INGREDIENTS_FILTER
import com.example.cocktailbank.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class CocktailsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: CocktailsBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipesViewModel: RecipesViewModel
    private var filterType = DEFAULT_FILTER_TYPE
    private var filterName = DEFAULT_FILTER_NAME
    private var filterId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = CocktailsBottomSheetBinding.inflate(layoutInflater)

        recipesViewModel.readFilter.asLiveData().observe(viewLifecycleOwner) { value ->
            filterType = value.selectedFilterType
            filterName = value.selectedFilterName
            filterId = value.selectedFilterId
            if (value.selectedFilterType.equals(CATEGORY_FILTER)) {
                updateChip(binding.categoryChipGroup, value.selectedFilterId)
            } else if (value.selectedFilterType.equals(GLASS_FILTER)) {
                updateChip(binding.glassChipGroup, value.selectedFilterId)
            } else if (value.selectedFilterType.equals(INGREDIENTS_FILTER)) {
                updateChip(binding.ingredientsChipGroup, value.selectedFilterId)
            } else if (value.selectedFilterType.equals(ALCOHOLIC_TYPE_FILTER)) {
                updateChip(binding.alcoholicTypeChipGroup, value.selectedFilterId)
            }
        }

        binding.categoryChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                binding.glassChipGroup.clearCheck()
                binding.ingredientsChipGroup.clearCheck()
                binding.alcoholicTypeChipGroup.clearCheck()
                val chip = group.findViewById<Chip>(checkedIds[0])
                val chipText = chip.text.toString().replace(" ", "_")
                val selectedFilterName = chipText
                filterType = CATEGORY_FILTER
                filterName = selectedFilterName
                filterId = checkedIds[0]
            }
        }

        binding.glassChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                binding.categoryChipGroup.clearCheck()
                binding.ingredientsChipGroup.clearCheck()
                binding.alcoholicTypeChipGroup.clearCheck()
                val chip = group.findViewById<Chip>(checkedIds[0])
                val chipText = chip.text.toString().replace(" ", "_")
                val selectedFilterName = chipText
                filterType = GLASS_FILTER
                filterName = selectedFilterName
                filterId = checkedIds[0]
            }

        }

        binding.ingredientsChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                binding.categoryChipGroup.clearCheck()
                binding.glassChipGroup.clearCheck()
                binding.alcoholicTypeChipGroup.clearCheck()
                val chip = group.findViewById<Chip>(checkedIds[0])
                val chipText = chip.text.toString().replace(" ", "_")
                val selectedFilterName = chipText
                filterType = INGREDIENTS_FILTER
                filterName = selectedFilterName
                filterId = checkedIds[0]
            }
        }

        binding.alcoholicTypeChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                binding.categoryChipGroup.clearCheck()
                binding.glassChipGroup.clearCheck()
                binding.ingredientsChipGroup.clearCheck()
                val chip = group.findViewById<Chip>(checkedIds[0])
                val chipText = chip.text.toString().replace(" ", "_")
                val selectedFilterName = chipText
                filterType = ALCOHOLIC_TYPE_FILTER
                filterName = selectedFilterName
                filterId = checkedIds[0]
            }
        }

        binding.applyButton.setOnClickListener {
            recipesViewModel.saveFilter(filterType, filterName, filterId)
            val action =
                CocktailsBottomSheetDirections.actionCocktailsBottomSheetToCocktailsFragment(true)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun updateChip(chipGroup: ChipGroup, selectedFilterId: Int) {
        if (selectedFilterId != 0) {
            try {
                //Focus on chip selected
                val targetView = chipGroup.findViewById<Chip>(selectedFilterId)
                targetView.isChecked = true
                chipGroup.requestChildFocus(targetView, targetView)

            } catch (e: Exception) {
                Log.d("CocktailsBottomSheet", e.message.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}