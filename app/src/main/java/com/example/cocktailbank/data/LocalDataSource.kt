package com.example.cocktailbank.data

import com.example.cocktailbank.data.database.CocktailsDao
import com.example.cocktailbank.data.database.entities.CocktailsEntity
import com.example.cocktailbank.data.database.entities.FavoriteCocktailsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val cocktailsDao: CocktailsDao
) {

    fun readCocktails(): Flow<List<CocktailsEntity>> {
        return cocktailsDao.readCocktails()
    }

    fun readFavoriteCocktails(): Flow<List<FavoriteCocktailsEntity>> {
        return cocktailsDao.readFavoriteCocktails()
    }

    suspend fun insertCocktails(cocktailsEntity: CocktailsEntity) {
        cocktailsDao.insertCocktails(cocktailsEntity)
    }

    suspend fun insertFavoriteCocktails(favoriteCocktailsEntity: FavoriteCocktailsEntity) {
        cocktailsDao.insertFavoriteCocktails(favoriteCocktailsEntity)
    }

    suspend fun deleteFavoriteCocktail(favoriteCocktailsEntity: FavoriteCocktailsEntity) {
        cocktailsDao.deleteFavoriteCocktail(favoriteCocktailsEntity)
    }

    suspend fun deleteAllFavoriteCocktails() {
        cocktailsDao.deleteAllFavoriteCocktails()
    }
}