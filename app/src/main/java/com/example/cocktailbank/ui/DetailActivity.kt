package com.example.cocktailbank.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.cocktailbank.R
import com.example.cocktailbank.adapters.PagerAdapter
import com.example.cocktailbank.data.database.entities.FavoriteCocktailsEntity
import com.example.cocktailbank.databinding.ActivityDetailBinding
import com.example.cocktailbank.models.Drink
import com.example.cocktailbank.ui.fragment.CocktailIngredients.CocktailIngredientsFragment
import com.example.cocktailbank.ui.fragment.CocktailOverview.CocktailOverviewFragment
import com.example.cocktailbank.util.Constants.Companion.BUNDLE_KEY_1
import com.example.cocktailbank.util.NetworkResult
import com.example.cocktailbank.viewmodels.MainViewModel
import com.example.cocktailbank.viewmodels.RecipesViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val args by navArgs<DetailActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private val recipesViewModel: RecipesViewModel by viewModels()

    private lateinit var currentDrink: Drink
    private var cocktailSaved = false
    private var savedCocktailId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.detailToolbar)
        binding.detailToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        requestApiDataById(args.drink)
    }

    private fun requestApiDataById(drink: Drink) {
        mainViewModel.getRecipeById(recipesViewModel.applyQueriesId(drink.idDrink))
        mainViewModel.recipesResponseById.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        val fragments = ArrayList<Fragment>()
                        fragments.add(CocktailOverviewFragment())
                        fragments.add(CocktailIngredientsFragment())

                        val titles = ArrayList<String>()
                        titles.add("Overview")
                        titles.add("Ingredients")

                        val drinkBundle = Bundle()
                        drinkBundle.putParcelable(BUNDLE_KEY_1, it.drinks!![0])

                        currentDrink = it.drinks[0]

                        val pagerAdapter = PagerAdapter(
                            drinkBundle,
                            fragments,
                            this
                        )

                        binding.viewPager2.isUserInputEnabled = false
                        binding.viewPager2.adapter = pagerAdapter
                        TabLayoutMediator(binding.detailTabLayout, binding.viewPager2) { tab, position ->
                            tab.text = titles[position]
                        }.attach()
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        this,
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {

                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedCocktails(menuItem!!)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        else if (item.itemId == R.id.save_to_favorites_menu && !cocktailSaved) {
            saveToFavorites(item)
        }
        else if (item.itemId == R.id.save_to_favorites_menu && cocktailSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedCocktails(menuItem: MenuItem) {
        mainViewModel.readFavoriteCocktails.observe(this) { favoriteCocktailsEntity ->
            try {
                for (savedCocktail in favoriteCocktailsEntity) {
                    if (savedCocktail.drink.idDrink == args.drink.idDrink) {
                        changeMenuItemColor(menuItem, R.color.red)
                        savedCocktailId = savedCocktail.id
                        cocktailSaved = true
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailActivity", e.message.toString())
            }
        }
    }

    private fun saveToFavorites(item: MenuItem) {
        val favoriteCocktailsEntity = FavoriteCocktailsEntity(
            0,
            currentDrink
        )
        mainViewModel.insertFavoriteCocktails(favoriteCocktailsEntity)
        changeMenuItemColor(item, R.color.red)
        showSnackBar("Cocktail Saved!")
        cocktailSaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoriteCocktailsEntity = FavoriteCocktailsEntity(
            savedCocktailId,
            currentDrink
        )
        mainViewModel.deleteFavoriteCocktail(favoriteCocktailsEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Cocktail Deleted!")
        cocktailSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Ok"){}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon?.setTint(ContextCompat.getColor(this, color))
    }
}