package com.example.cocktailbank.data.database

import androidx.room.*
import com.example.cocktailbank.data.database.entities.CocktailsEntity
import com.example.cocktailbank.data.database.entities.FavoriteCocktailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktails(cocktailsEntity: CocktailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCocktails(favoriteCocktailsEntity: FavoriteCocktailsEntity)

    @Query("SELECT * FROM cocktails_table ORDER BY id ASC")
    fun readCocktails(): Flow<List<CocktailsEntity>>

    @Query("SELECT * FROM favorite_cocktails_table ORDER BY id ASC")
    fun readFavoriteCocktails(): Flow<List<FavoriteCocktailsEntity>>

    @Delete
    suspend fun deleteFavoriteCocktail(favoriteCocktailsEntity: FavoriteCocktailsEntity)

    @Query("DELETE FROM favorite_cocktails_table")
    suspend fun deleteAllFavoriteCocktails()

}