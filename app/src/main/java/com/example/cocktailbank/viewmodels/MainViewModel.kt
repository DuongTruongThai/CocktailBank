package com.example.cocktailbank.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.cocktailbank.MyApplication
import com.example.cocktailbank.data.Repository
import com.example.cocktailbank.data.cache.RecipeDetailCache
import com.example.cocktailbank.data.database.entities.CocktailsEntity
import com.example.cocktailbank.data.database.entities.FavoriteCocktailsEntity
import com.example.cocktailbank.models.CocktailsRecipe
import com.example.cocktailbank.models.IngredientList
import com.example.cocktailbank.util.Constants
import com.example.cocktailbank.util.NetworkResult
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val application: MyApplication
) : ViewModel() {

    /** ROOM DATABASE **/
    val readCocktails: LiveData<List<CocktailsEntity>> =
        repository.local.readCocktails().asLiveData()
    val readFavoriteCocktails: LiveData<List<FavoriteCocktailsEntity>> =
        repository.local.readFavoriteCocktails().asLiveData()

    fun insertCocktails(cocktailsEntity: CocktailsEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertCocktails(cocktailsEntity)
    }

    fun insertFavoriteCocktails(favoriteCocktailsEntity: FavoriteCocktailsEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteCocktails(favoriteCocktailsEntity)
        }

    fun deleteFavoriteCocktail(favoriteCocktailsEntity: FavoriteCocktailsEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteCocktail(favoriteCocktailsEntity)
        }

    fun deleteAllFavoriteCocktails() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteCocktails()
        }

    /** RETROFIT **/
    var recipesResponseByFilter: MutableLiveData<NetworkResult<CocktailsRecipe>> = MutableLiveData()
    var recipesResponseById: MutableLiveData<NetworkResult<CocktailsRecipe>> = MutableLiveData()

    var searchRecipesResponse: MutableLiveData<NetworkResult<CocktailsRecipe>> = MutableLiveData()

    var ingredientListResponse: MutableLiveData<NetworkResult<CocktailsRecipe>> = MutableLiveData()

    var searchIngredientResponse: MutableLiveData<NetworkResult<IngredientList>> = MutableLiveData()

    fun getRecipeById(queries: Map<String, String>) = viewModelScope.launch {
        getRecipeByIdSafeCall(queries)
    }

    fun getRecipeByFilter(queries: Map<String, String>) = viewModelScope.launch {
        getRecipeByFilterSafeCall(queries)
    }

    fun searchRecipesByName(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesByNameSafeCall(searchQuery)
    }

    fun getIngredientList(queries: Map<String, String>) = viewModelScope.launch {
        getIngredientListSafeCall(queries)
    }

    fun searchIngredient(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchIngredientSafeCall(searchQuery)
    }

    private suspend fun getRecipeByIdSafeCall(queries: Map<String, String>) {

        //Check cache for recipe detail
        val cache = RecipeDetailCache.recipeDetailMap[queries[Constants.QUERY_ID]]
        if (cache != null) {
            recipesResponseById.value = NetworkResult.Success(cache)
            return
        }

        //If cache is empty
        recipesResponseById.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipeById(queries)
                recipesResponseById.value = handleCocktailsRecipeResponse(response)
                RecipeDetailCache.recipeDetailMap[queries[Constants.QUERY_ID].toString()] = response.body()!!
            } catch (e: Exception) {
                recipesResponseById.value = NetworkResult.Error("Recipe Not Found!")
            }
        } else {
            recipesResponseById.value = NetworkResult.Error("No Internet Connection!")
        }
    }

    private suspend fun getRecipeByFilterSafeCall(queries: Map<String, String>) {
        recipesResponseByFilter.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipeByFilter(queries)
                recipesResponseByFilter.value = handleCocktailsRecipeResponse(response)

                val cocktailsRecipe = recipesResponseByFilter.value!!.data
                if (cocktailsRecipe != null) {
                    offlineCacheCocktails(cocktailsRecipe)
                }
            } catch (e: Exception) {
                recipesResponseByFilter.value = NetworkResult.Error("Recipe Not Found!")
            }
        } else {
            recipesResponseByFilter.value = NetworkResult.Error("No Internet Connection!")
        }
    }

    private suspend fun searchRecipesByNameSafeCall(searchQuery: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipesByName(searchQuery)
                searchRecipesResponse.value = handleCocktailsRecipeResponse(response)

            } catch (e: Exception) {
                searchRecipesResponse.value = NetworkResult.Error("Recipe Not Found!")
            }
        } else {
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection!")
        }
    }

    private suspend fun getIngredientListSafeCall(queries: Map<String, String>) {
        ingredientListResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getIngredientList(queries)
                ingredientListResponse.value = handleCocktailsRecipeResponse(response)

            } catch (e: Exception) {
                ingredientListResponse.value = NetworkResult.Error("Ingredient Not Found!")
            }
        } else {
            ingredientListResponse.value = NetworkResult.Error("No Internet Connection!")
        }
    }

    private suspend fun searchIngredientSafeCall(searchQuery: Map<String, String>) {
        searchIngredientResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchIngredient(searchQuery)
                searchIngredientResponse.value = handleSearchIngredientResponse(response)

            } catch (e: Exception) {
                searchIngredientResponse.value = NetworkResult.Error("Ingredient Not Found!")
            }
        } else {
            searchIngredientResponse.value = NetworkResult.Error("No Internet Connection!")
        }
    }

    private fun offlineCacheCocktails(cocktailsRecipe: CocktailsRecipe) {
        val cocktailsEntity = CocktailsEntity(cocktailsRecipe)
        insertCocktails(cocktailsEntity)
    }

    private fun handleCocktailsRecipeResponse(response: Response<CocktailsRecipe>): NetworkResult<CocktailsRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.drinks.isNullOrEmpty() -> {
                return NetworkResult.Error("Not found.")
            }
            response.isSuccessful -> {
                val cocktailsRecipe = response.body()
                return NetworkResult.Success(cocktailsRecipe!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleSearchIngredientResponse(response: Response<IngredientList>): NetworkResult<IngredientList>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.ingredients.isNullOrEmpty() -> {
                return NetworkResult.Error("Ingredient not found.")
            }
            response.isSuccessful -> {
                val ingredientList = response.body()
                return NetworkResult.Success(ingredientList!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = Contexts.getApplication(application).getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}