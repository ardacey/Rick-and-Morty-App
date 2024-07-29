package com.example.rickandmorty.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
private val FAVORITE_CHARACTERS_KEY = stringSetPreferencesKey("favorite_characters")
private val FAVORITE_LOCATIONS_KEY = stringSetPreferencesKey("favorite_locations")
private val FAVORITE_EPISODES_KEY = stringSetPreferencesKey("favorite_episodes")
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(context: Context) {

    private val dataStore = context.dataStore

    private fun <T> getPreferences(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }

    val darkModeFlow: Flow<Boolean> = getPreferences(DARK_MODE_KEY, false)
    val favoriteCharactersFlow: Flow<Set<String>> = getPreferences(FAVORITE_CHARACTERS_KEY, emptySet())
    val favoriteLocationsFlow: Flow<Set<String>> = getPreferences(FAVORITE_LOCATIONS_KEY, emptySet())
    val favoriteEpisodesFlow: Flow<Set<String>> = getPreferences(FAVORITE_EPISODES_KEY, emptySet())

    private suspend fun modifySetPreference(key: Preferences.Key<Set<String>>, item: String, add: Boolean) {
        dataStore.edit { preferences ->
            val set = preferences[key]?.toMutableSet() ?: mutableSetOf()
            if (add) set.add(item) else set.remove(item)
            preferences[key] = set
        }
    }

    suspend fun updateDarkMode(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isDarkMode
        }
    }

    suspend fun addFavoriteCharacter(characterId: String) = modifySetPreference(FAVORITE_CHARACTERS_KEY, characterId, add = true)
    suspend fun removeFavoriteCharacter(characterId: String) = modifySetPreference(FAVORITE_CHARACTERS_KEY, characterId, add = false)

    suspend fun addFavoriteLocation(locationId: String) = modifySetPreference(FAVORITE_LOCATIONS_KEY, locationId, add = true)
    suspend fun removeFavoriteLocation(locationId: String) = modifySetPreference(FAVORITE_LOCATIONS_KEY, locationId, add = false)

    suspend fun addFavoriteEpisode(episodeId: String) = modifySetPreference(FAVORITE_EPISODES_KEY, episodeId, add = true)
    suspend fun removeFavoriteEpisode(episodeId: String) = modifySetPreference(FAVORITE_EPISODES_KEY, episodeId, add = false)
}
