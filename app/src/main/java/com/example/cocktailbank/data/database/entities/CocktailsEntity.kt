package com.example.cocktailbank.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cocktailbank.models.CocktailsRecipe
import com.example.cocktailbank.util.Constants.Companion.COCKTAILS_TABLE

@Entity(tableName = COCKTAILS_TABLE)
class CocktailsEntity(
    var cocktailsRecipe: CocktailsRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}