package com.example.cocktailbank.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cocktailbank.data.DataStoreRepository
import com.example.cocktailbank.data.Filter
import com.example.cocktailbank.util.Constants.Companion.DEFAULT_FILTER_NAME
import com.example.cocktailbank.util.Constants.Companion.DEFAULT_FILTER_TYPE
import com.example.cocktailbank.util.Constants.Companion.QUERY_ID
import com.example.cocktailbank.util.Constants.Companion.QUERY_INGREDIENT
import com.example.cocktailbank.util.Constants.Companion.QUERY_SEARCH_BY_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val application: Application,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    private var filterType = DEFAULT_FILTER_TYPE
    private var filterName = DEFAULT_FILTER_NAME

    //For checking network status
    var networkStatus = false
    var backOnline = false

    val readFilter = dataStoreRepository.readFilter
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveFilter(filterType: String, filterName: String, filterId: Int) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveFilter(filterType, filterName, filterId)
    }

    fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun applyQueriesFilter(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readFilter.collectLatest { value ->
                filterType = value.selectedFilterType
                filterName = value.selectedFilterName
            }
        }

        queries[filterType] = filterName
        return queries
    }

    fun applyQueriesId(idDrink: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_ID] = idDrink
        return queries
    }

    fun applySearchCocktailQuery(searchText: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_SEARCH_BY_NAME] = searchText
        return queries
    }

    fun applyQueriesIngredientList(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_INGREDIENT] = "list"
        return queries
    }

    fun applySearchIngredientQuery(searchText: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_INGREDIENT] = searchText
        return queries
    }

    //For checking network status
    fun showNetworkStatus() {
        if (!networkStatus) {
            if (!backOnline) {
                Toast.makeText(application, "No Internet Connection!", Toast.LENGTH_SHORT).show()
                saveBackOnline(true)
            }
        }
        else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(application, "Internet Connection Available!", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}