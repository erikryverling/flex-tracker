package se.yverling.flextracker

import androidx.annotation.VisibleForTesting
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel

// TODO Launch icons 3. Refactor and lint 4. GitHub and install APK
class MainViewModel(@VisibleForTesting var minutes: Int) : ViewModel() {
    val events = SingleLiveEvent<Event>()

    val formattedValue: ObservableField<String> = ObservableField()
    val isPositive: ObservableBoolean = ObservableBoolean()

    init {
        toggleIsPositive()
        formatValue()
    }

    fun onDecreaseButtonClick() {
        minutes -= 30

        persistValue()
        toggleIsPositive()
        formatValue()
    }

    fun onIncreaseButtonClick() {
        minutes += 30

        persistValue()
        toggleIsPositive()
        formatValue()
    }

    private fun toggleIsPositive() {
        if (minutes < 0) {
            isPositive.set(false)
        } else {
            isPositive.set(true)
        }
    }

    private fun persistValue() {
        events.value = Event.Persist(minutes)
    }

    private fun formatValue() {
        val hours = minutes / 60
        val isFullHour = minutes % 60 == 0

        when {
            minutes == 0 -> formattedValue.set("0 h")
            minutes == 30 -> formattedValue.set("30 m")
            minutes == -30 -> formattedValue.set("-30 m")
            isFullHour -> formattedValue.set("$hours h")
            else -> formattedValue.set("$hours h 30 m")
        }
    }

    sealed class Event {
        data class Persist(val minutes: Int) : Event()
    }
}