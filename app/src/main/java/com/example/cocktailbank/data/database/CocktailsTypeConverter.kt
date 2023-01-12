package com.example.cocktailbank.data.database

import androidx.room.TypeConverter
import com.example.cocktailbank.models.CocktailsRecipe
import com.example.cocktailbank.models.Drink
import com.google.gson.Gson

class CocktailsTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun cocktailsRecipeToString(cocktailsRecipe: CocktailsRecipe): String =
        gson.toJson(cocktailsRecipe)

    @TypeConverter
    fun stringToCocktailsRecipe(data: String): CocktailsRecipe =
        gson.fromJson(data, CocktailsRecipe::class.java)

    @TypeConverter
    fun drinkToString(drink: Drink): String = gson.toJson(drink)

    @TypeConverter
    fun stringToDrink(data: String): Drink = gson.fromJson(data, Drink::class.java)
}