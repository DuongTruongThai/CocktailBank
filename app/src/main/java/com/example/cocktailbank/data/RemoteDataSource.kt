package com.example.cocktailbank.data

import com.example.cocktailbank.data.network.CocktailsRecipeApi
import com.example.cocktailbank.models.CocktailsRecipe
import com.example.cocktailbank.models.IngredientList
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val cocktailsRecipeApi: CocktailsRecipeApi
) {

    suspend fun getRecipeById(queries: Map<String, String>): Response<CocktailsRecipe> {
        return cocktailsRecipeApi.getRecipeById(queries)
    }

    suspend fun getRecipeByFilter(queries: Map<String, String>): Response<CocktailsRecipe> {
        return cocktailsRecipeApi.getRecipeByFilter(queries)
    }

    suspend fun searchRecipesByName(searchQueries: Map<String, String>): Response<CocktailsRecipe> {
        return cocktailsRecipeApi.searchRecipeByName(searchQueries)
    }

    suspend fun getIngredientList(queries: Map<String, String>): Response<CocktailsRecipe> {
        return cocktailsRecipeApi.getIngredientList(queries)
    }

    suspend fun searchIngredient(searchQueries: Map<String, String>): Response<IngredientList> {
        return cocktailsRecipeApi.searchIngredient(searchQueries)
    }
}