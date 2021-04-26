package com.burf.favdish.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.burf.favdish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
    fun getAllDishesList(): Flow<List<FavDish>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavDishDetails(favDish: FavDish)

//    @Query("DELETE FROM word_table")
//    suspend fun deleteAll()
}
