package com.example.cocktailbank.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cocktailbank.data.database.entities.CocktailsEntity
import com.example.cocktailbank.data.database.entities.FavoriteCocktailsEntity

@Database(
    entities = [CocktailsEntity::class, FavoriteCocktailsEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CocktailsTypeConverter::class)
abstract class CocktailsDatabase: RoomDatabase() {

    abstract fun cocktailsDao(): CocktailsDao

}