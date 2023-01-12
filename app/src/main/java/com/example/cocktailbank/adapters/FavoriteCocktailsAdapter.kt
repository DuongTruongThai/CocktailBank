package com.example.cocktailbank.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cocktailbank.R
import com.example.cocktailbank.data.database.entities.FavoriteCocktailsEntity
import com.example.cocktailbank.databinding.FavoriteCocktailsRowLayoutBinding
import com.example.cocktailbank.ui.fragment.FavoriteCocktails.FavoriteCocktailsFragmentDirections
import com.example.cocktailbank.util.CocktailRecipesDiffUtil
import com.example.cocktailbank.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteCocktailsAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoriteCocktailsAdapter.MyViewHolder>(), ActionMode.Callback {

    private var favoriteCocktailsList = emptyList<FavoriteCocktailsEntity>()

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View

    private var myViewHolder = arrayListOf<MyViewHolder>()
    private var multiSelection = false
    private var selectedCocktails = arrayListOf<FavoriteCocktailsEntity>()

    class MyViewHolder(val binding: FavoriteCocktailsRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteCocktailsEntity: FavoriteCocktailsEntity) {
            binding.favoriteCocktailsEntity = favoriteCocktailsEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FavoriteCocktailsRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        myViewHolder.add(holder)
        rootView = holder.binding.root

        val currentCocktails = favoriteCocktailsList[position]
        holder.bind(currentCocktails)

        //Fix contextual when scrolling
        saveItemStateOnScroll(holder, currentCocktails)

        //Single click listener
        holder.binding.favoriteCocktailsRowLayout.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentCocktails)
            }
            else {
                val action =
                    FavoriteCocktailsFragmentDirections.actionFavoriteCocktailsFragmentToDetailActivity(
                        currentCocktails.drink
                    )
                holder.binding.root.findNavController().navigate(action)
            }
        }

        //Long click listener
        holder.binding.favoriteCocktailsRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentCocktails)
                true
            }
            else {
                applySelection(holder, currentCocktails)
                true
            }
        }
    }

    private fun saveItemStateOnScroll(holder: MyViewHolder, currentCocktail: FavoriteCocktailsEntity) {
        if (selectedCocktails.contains(currentCocktail)) {
            changeCocktailStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
        } else {
            changeCocktailStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
    }

    private fun applySelection(holder: MyViewHolder, currentCocktail: FavoriteCocktailsEntity) {
        if (selectedCocktails.contains(currentCocktail)) {
            selectedCocktails.remove(currentCocktail)
            changeCocktailStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedCocktails.add(currentCocktail)
            changeCocktailStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeCocktailStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.binding.favoriteCocktailsRowLayout.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                backgroundColor
            )
        )
        holder.binding.favoriteCocktailCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    //Whenever selectedCocktails size changes
    private fun applyActionModeTitle() {
        when(selectedCocktails.size) {
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedCocktails.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedCocktails.size} items selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteCocktailsList.size
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorite_cocktails_contextual_menu, menu)
        mActionMode = actionMode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menuItem: MenuItem?): Boolean {
        if (menuItem?.itemId == R.id.delete_favorite_cocktails_menu) {
            selectedCocktails.forEach {
                mainViewModel.deleteFavoriteCocktail(it)
            }
            showSnackBar("${selectedCocktails.size} item/s removed!")
            multiSelection = false
            selectedCocktails.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolder.forEach { holder ->
            changeCocktailStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        applyStatusBarColor(R.color.statusBarColor)
        selectedCocktails.clear()
        multiSelection = false
    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newFavoriteCocktailsList: List<FavoriteCocktailsEntity>) {
        val favoriteCocktailsDiffUtil =
            CocktailRecipesDiffUtil(favoriteCocktailsList, newFavoriteCocktailsList)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteCocktailsDiffUtil)
        favoriteCocktailsList = newFavoriteCocktailsList
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Ok") {}
            .show()
    }

    fun clearContextualActionMode() {
        if (this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }
}