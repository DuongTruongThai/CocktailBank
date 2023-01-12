package com.example.cocktailbank.data.network

import com.example.cocktailbank.models.CocktailsRecipe
import com.example.cocktailbank.models.IngredientList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface CocktailsRecipeApi {

    @GET("lookup.php")
    suspend fun getRecipeById(
        @QueryMap queries: Map<String, String>
    ): Response<CocktailsRecipe>

    @GET("filter.php")
    suspend fun getRecipeByFilter(
        @QueryMap queries: Map<String, String>
    ): Response<CocktailsRecipe>

    @GET("search.php")
    suspend fun searchRecipeByName(
        @QueryMap queries: Map<String, String>
    ): Response<CocktailsRecipe>

    @GET("list.php")
    suspend fun getIngredientList(
        @QueryMap queries: Map<String, String>
    ): Response<CocktailsRecipe>

    @GET("search.php")
    suspend fun searchIngredient(
        @QueryMap queries: Map<String, String>
    ): Response<IngredientList>
}