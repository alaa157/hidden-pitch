package hiddenpitch.ui.home

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import hiddenpitch.ui.theme.HiddenPitchTheme

@Preview(name = "Home / English / Light", locale = "en", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable private fun HomeEnglishLightPreview() { HiddenPitchTheme(false) { HomeContent(onOpenDetail = {}, onOpenAdd = {}, onOpenMyPlaces = {}, onOpenSettings = {}) } }

@Preview(name = "Home / English / Dark", locale = "en", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable private fun HomeEnglishDarkPreview() { HiddenPitchTheme(true) { HomeContent(onOpenDetail = {}, onOpenAdd = {}, onOpenMyPlaces = {}, onOpenSettings = {}) } }

@Preview(name = "Home / Arabic / Light", locale = "ar", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable private fun HomeArabicLightPreview() { HiddenPitchTheme(false) { HomeContent(onOpenDetail = {}, onOpenAdd = {}, onOpenMyPlaces = {}, onOpenSettings = {}) } }

@Preview(name = "Home / Arabic / Dark", locale = "ar", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable private fun HomeArabicDarkPreview() { HiddenPitchTheme(true) { HomeContent(onOpenDetail = {}, onOpenAdd = {}, onOpenMyPlaces = {}, onOpenSettings = {}) } }
