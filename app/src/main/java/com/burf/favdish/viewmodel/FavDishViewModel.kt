package com.burf.favdish.viewmodel

import androidx.lifecycle.*
import com.burf.favdish.model.database.FavDishRepository
import com.burf.favdish.model.entities.FavDish
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class FavDishViewModel(private val repository: FavDishRepository) : ViewModel() {

    fun insert(favDisk : FavDish) = viewModelScope.launch {
        repository.insertFavDishData(favDisk)
    }

    fun delete(favDisk : FavDish) = viewModelScope.launch {
        repository.deleteDishData(favDisk)
    }

    fun update(favDisk : FavDish) = viewModelScope.launch {
        repository.updateDishData(favDisk)
    }

    val allDishesList : LiveData<List<FavDish>> = repository.allDishesList.asLiveData()

    val favoriteDishesList : LiveData<List<FavDish>> = repository.favoriteDishesList.asLiveData()

    fun getFilteredList(value: String) : LiveData<List<FavDish>> = repository.filteredListDishes(value).asLiveData()
}

class FavDishViewModelFactory(private val repository: FavDishRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return FavDishViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}