package hiddenpitch.ui.settings

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import hiddenpitch.ui.theme.HiddenPitchTheme
import hiddenpitch.util.AppLanguageChoice

@Preview(name = "Settings / English / Light", locale = "en", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable private fun SettingsEnglishLightPreview() { HiddenPitchTheme(false) { SettingsContent(AppLanguageChoice.ENGLISH, {}, {}) } }

@Preview(name = "Settings / English / Dark", locale = "en", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable private fun SettingsEnglishDarkPreview() { HiddenPitchTheme(true) { SettingsContent(AppLanguageChoice.ENGLISH, {}, {}) } }

@Preview(name = "Settings / Arabic / Light", locale = "ar", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable private fun SettingsArabicLightPreview() { HiddenPitchTheme(false) { SettingsContent(AppLanguageChoice.ARABIC, {}, {}) } }

@Preview(name = "Settings / Arabic / Dark", locale = "ar", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable private fun SettingsArabicDarkPreview() { HiddenPitchTheme(true) { SettingsContent(AppLanguageChoice.ARABIC, {}, {}) } }
