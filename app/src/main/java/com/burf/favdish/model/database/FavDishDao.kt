package com.burf.favdish.model.database

import androidx.room.*
import com.burf.favdish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
    fun getAllDishesList(): Flow<List<FavDish>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Update
    suspend fun updateFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE favorite_dish = 1")
    fun getFavoriteDishesList(): Flow<List<FavDish>>

    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE type = :filterType")
    fun getFilteredDishesList(filterType: String): Flow<List<FavDish>>

    @Delete
    suspend fun deleteFavDishDetails(favDish: FavDish)
}
