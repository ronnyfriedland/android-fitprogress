package de.ronnyfriedland.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Application color palette
val Bg = Color(0xFF111513)
val Surface = Color(0xFF202723)
val Surface2 = Color(0xFF29332D)
val Accent = Color(0xFF28734C)
val TextMain = Color(0xFFF1F4F1)
val Muted = Color(0xFFAAB5AD)

/** Preconfigured text field colors with high contrast for dark mode. */
@Composable
fun readableFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color(0xFFF7FAF7),
    unfocusedTextColor = Color(0xFFF7FAF7),
    disabledTextColor = Color(0xFFB8C2BB),
    focusedLabelColor = Color(0xFF9AD6AE),
    unfocusedLabelColor = Color(0xFFD0D8D2),
    cursorColor = Color(0xFF9AD6AE),
    focusedBorderColor = Color(0xFF9AD6AE),
    unfocusedBorderColor = Color(0xFF9AA69E),
    focusedPlaceholderColor = Color(0xFFB8C2BB),
    unfocusedPlaceholderColor = Color(0xFFB8C2BB)
)

/** App-wide dark theme configuration. */
@Composable
fun FitProgressTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Bg,
            surface = Surface,
            primary = Accent,
            onBackground = TextMain,
            onSurface = TextMain,
            secondary = Color(0xFF75B58E),
            onSurfaceVariant = Color(0xFFD0D8D2),
            outline = Color(0xFF829087)
        ),
        content = content
    )
}
