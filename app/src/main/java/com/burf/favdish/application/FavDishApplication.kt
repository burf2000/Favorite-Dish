package com.burf.favdish.application

import android.app.Application
import com.burf.favdish.model.database.FavDishRepository
import com.burf.favdish.model.database.FavDishRoomDatabase

class FavDishApplication : Application() {

    private val database by lazy {
        FavDishRoomDatabase.getDatabase(this@FavDishApplication)
    }

    val repository by lazy {
        FavDishRepository(database.favDishDao())
    }
}