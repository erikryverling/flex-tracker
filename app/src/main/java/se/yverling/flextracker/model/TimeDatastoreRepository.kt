package se.yverling.flextracker.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TimeDataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private val timeInMinutesKey = intPreferencesKey("time_in_minutes")

    fun read(): Flow<Int> {
        return context.currentTimeDataStore.data
            .map { settings ->
                settings[timeInMinutesKey] ?: 0
            }
    }

    suspend fun write(timeInMinutes: Int) {
        context.currentTimeDataStore.edit { settings ->
            settings[timeInMinutesKey] = timeInMinutes
        }
    }
}

const val DATASTORE_NAME = "settings"
val Context.currentTimeDataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)