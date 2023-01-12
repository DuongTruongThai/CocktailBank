package com.example.cocktailbank.models


import com.google.gson.annotations.SerializedName

data class CocktailsRecipe(
    @SerializedName("drinks")
    val drinks: MutableList<Drink>?
)