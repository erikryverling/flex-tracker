package se.yverling.flextracker

import android.app.Application
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import se.yverling.flextracker.model.TimeDataStoreRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    app: Application,
    private val repository: TimeDataStoreRepository
) : AndroidViewModel(app) {

    private val mutableUiState: MutableStateFlow<MainUiState> =
        MutableStateFlow(MainUiState.Loading)

    internal var uiState: StateFlow<MainUiState> = mutableUiState

    private val vibrator: Vibrator by lazy {
        app.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun read() {
        mutableUiState.value = MainUiState.Loading

        viewModelScope.launch {
            repository.read().collect { timeInMinutes ->
                mutableUiState.value = MainUiState.Success(timeInMinutes)
            }
        }
    }

    fun write(timeInMinutes: Int) {
        viewModelScope.launch {
            repository.write(timeInMinutes)
        }
    }

    fun vibrateTick() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        }
    }
}

sealed class MainUiState(val data: Any? = null) {
    object Loading : MainUiState()
    data class Success(val timeInMinutes: Int) : MainUiState(timeInMinutes)
}