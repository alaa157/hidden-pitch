package hiddenpitch.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
fun isRtl(): Boolean = LocalLayoutDirection.current == LayoutDirection.Rtl
