package hiddenpitch.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

internal val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1F6B45), onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD1F0DD), onPrimaryContainer = Color(0xFF002113),
    secondary = Color(0xFF99502E), onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDBCA), onSecondaryContainer = Color(0xFF351000),
    background = Color(0xFFF8FAF7), onBackground = Color(0xFF171D19),
    surface = Color(0xFFF8FAF7), onSurface = Color(0xFF171D19),
    surfaceVariant = Color(0xFFDDE5DE), onSurfaceVariant = Color(0xFF414943),
    outline = Color(0xFF727A73)
)

internal val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF95D5B2), onPrimary = Color(0xFF003920),
    primaryContainer = Color(0xFF0A5235), onPrimaryContainer = Color(0xFFB1F0CD),
    secondary = Color(0xFFFFB596), onSecondary = Color(0xFF5D1D00),
    secondaryContainer = Color(0xFF7B3417), onSecondaryContainer = Color(0xFFFFDBCA),
    background = Color(0xFF101511), onBackground = Color(0xFFE0E5DF),
    surface = Color(0xFF101511), onSurface = Color(0xFFE0E5DF),
    surfaceVariant = Color(0xFF414943), onSurfaceVariant = Color(0xFFC1C9C1),
    outline = Color(0xFF8B938B)
)
