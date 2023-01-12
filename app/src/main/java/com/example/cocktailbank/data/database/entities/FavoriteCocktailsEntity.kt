package com.example.cocktailbank.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cocktailbank.models.Drink
import com.example.cocktailbank.util.Constants.Companion.FAVORITE_COCKTAILS_TABLE

@Entity(FAVORITE_COCKTAILS_TABLE)
class FavoriteCocktailsEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var drink: Drink
)