package com.example.cocktailbank.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.cocktailbank.util.Constants.Companion.DEFAULT_FILTER_NAME
import com.example.cocktailbank.util.Constants.Companion.DEFAULT_FILTER_TYPE
import com.example.cocktailbank.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.example.cocktailbank.util.Constants.Companion.PREFERENCES_FILTER_NAME
import com.example.cocktailbank.util.Constants.Companion.PREFERENCES_FILTER_ID
import com.example.cocktailbank.util.Constants.Companion.PREFERENCES_FILTER_TYPE
import com.example.cocktailbank.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val selected_filter_type = stringPreferencesKey(PREFERENCES_FILTER_TYPE)
        val selected_filter_name = stringPreferencesKey(PREFERENCES_FILTER_NAME)
        val selected_filter_id = intPreferencesKey(PREFERENCES_FILTER_ID)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

    val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveFilter(filterType: String, filterName: String, filterId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.selected_filter_type] = filterType
            preferences[PreferenceKeys.selected_filter_name] = filterName
            preferences[PreferenceKeys.selected_filter_id] = filterId
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline] = backOnline
        }
    }

    val readFilter: Flow<Filter> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
            else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedFilterType = preferences[PreferenceKeys.selected_filter_type] ?: DEFAULT_FILTER_TYPE
            val selectedFilterName = preferences[PreferenceKeys.selected_filter_name] ?: DEFAULT_FILTER_NAME
            val selectedFilterId = preferences[PreferenceKeys.selected_filter_id] ?: 0
            Filter(selectedFilterType, selectedFilterName, selectedFilterId)
        }

    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            }
            else {
                throw exception
            }
        }
        .map { preferences ->
            val  backOnine = preferences[PreferenceKeys.backOnline] ?: false
            backOnine
        }

}

data class Filter(
    val selectedFilterType: String,
    val selectedFilterName: String,
    val selectedFilterId: Int
)