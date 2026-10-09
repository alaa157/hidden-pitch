package hiddenpitch.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    bodyLarge = TextStyle(fontFamily = FontFamily.Default, fontSize = 16.sp, lineHeight = 28.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.Default, fontSize = 14.sp, lineHeight = 26.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.Default, fontSize = 22.sp, lineHeight = 36.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.Default, fontSize = 18.sp, lineHeight = 30.sp),
    headlineSmall = TextStyle(fontFamily = FontFamily.Default, fontSize = 24.sp, lineHeight = 38.sp)
)
