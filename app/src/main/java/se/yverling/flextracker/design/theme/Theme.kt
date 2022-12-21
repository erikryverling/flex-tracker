package se.yverling.flextracker.design.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Typography

val FlexTrackerTypography = Typography(
    display1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 56.sp,
    )
)

@Composable
fun FlexTrackerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = FlexTrackerTypography,
        content = content
    )
}