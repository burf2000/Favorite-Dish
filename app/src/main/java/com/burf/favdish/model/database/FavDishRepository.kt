package com.burf.favdish.model.database
import androidx.annotation.WorkerThread
import com.burf.favdish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class FavDishRepository(private val favDishDao: FavDishDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allDishesList: Flow<List<FavDish>> = favDishDao.getAllDishesList()

    val favoriteDishesList: Flow<List<FavDish>> = favDishDao.getFavoriteDishesList()

    fun filteredListDishes(value : String) : Flow<List<FavDish>> = favDishDao.getFilteredDishesList(value)

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish) {
        favDishDao.insertFavDishDetails(favDish)
    }

    @WorkerThread
    suspend fun  updateDishData(favDish: FavDish) {
        favDishDao.updateFavDishDetails(favDish)
    }

    @WorkerThread
    suspend fun  deleteDishData(favDish: FavDish) {
        favDishDao.deleteFavDishDetails(favDish)
    }
}