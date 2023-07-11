package se.yverling.flextracker

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.RotaryScrollEvent
import androidx.compose.ui.input.rotary.onPreRotaryScrollEvent
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import se.yverling.flextracker.design.theme.FlexTrackerTheme
import se.yverling.flextracker.design.theme.LightGreen
import se.yverling.flextracker.design.theme.LightRed
import se.yverling.flextracker.design.theme.NormalSpace
import kotlin.math.abs

private const val TIME_CHANGE_STEP_IN_MINUTES = 15
private const val EVENTS_THRESHOLD = 8

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    FlexTrackerTheme {
        val uiState = viewModel.uiState.collectAsState()

        when (uiState.value) {
            MainUiState.Loading -> LoadingScreen()

            is MainUiState.Success -> {
                val timeInMinutes = (uiState.value as MainUiState.Success).timeInMinutes

                SuccessScreen(timeInMinutes) {
                    viewModel.vibrateTick()
                    viewModel.write(it)
                }
            }
        }
    }

    viewModel.read()
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun SuccessScreen(timeInMinutes: Int, onTimeChange: (Int) -> Unit) {
    // TODO Put a limit to value to be -99:45 - 99:45 to not overflow the string
    var currentTimeInMinutes by remember { mutableStateOf(timeInMinutes) }
    val focusRequester = remember { FocusRequester() }
    var upEvents by remember { mutableStateOf(0) }
    var downEvents by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .onPreRotaryScrollEvent {
                if (isScrollingUp(it)) upEvents++ else downEvents++

                if (upEvents > EVENTS_THRESHOLD || downEvents > EVENTS_THRESHOLD) {
                    if (isScrollingUp(it)) {
                        currentTimeInMinutes += TIME_CHANGE_STEP_IN_MINUTES
                        upEvents = 0
                    } else {
                        currentTimeInMinutes -= TIME_CHANGE_STEP_IN_MINUTES
                        downEvents = 0
                    }
                    onTimeChange(currentTimeInMinutes)
                }
                true
            }
            .focusRequester(focusRequester)
            .focusable()
            .padding(NormalSpace),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.padding(bottom = NormalSpace),
            color = if (currentTimeInMinutes < 0) LightRed else LightGreen,
            text = formatTime(currentTimeInMinutes),
            style = MaterialTheme.typography.display1,
            textAlign = TextAlign.Center
        )
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun isScrollingUp(it: RotaryScrollEvent) = it.verticalScrollPixels > 0

private fun formatTime(timeInMinutes: Int): AnnotatedString {
    val hours = "%02d".format(abs(timeInMinutes) / 60)
    val minutes = "%02d".format(abs(timeInMinutes) % 60)

    return AnnotatedString("$hours:$minutes")
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun LoadingPreview() {
    LoadingScreen()
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun PositiveTimePreview() {
    SuccessScreen(315, {})
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun NegativeTimePreview() {
    SuccessScreen(-630, {})
}