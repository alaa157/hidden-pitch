package hiddenpitch.util

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import hiddenpitch.R

enum class AppLanguageChoice(@StringRes val labelRes: Int) {
    SYSTEM(R.string.settings_language_system_default),
    ARABIC(R.string.settings_language_arabic),
    ENGLISH(R.string.settings_language_english)
}

class LocaleManager {
    val currentChoice: AppLanguageChoice
        get() {
            val locales = AppCompatDelegate.getApplicationLocales()
            if (locales.isEmpty) return AppLanguageChoice.SYSTEM
            return if (locales[0]?.language == "ar") AppLanguageChoice.ARABIC else AppLanguageChoice.ENGLISH
        }

    fun setChoice(choice: AppLanguageChoice) {
        val locales = when (choice) {
            AppLanguageChoice.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
            AppLanguageChoice.ARABIC -> LocaleListCompat.forLanguageTags("ar")
            AppLanguageChoice.ENGLISH -> LocaleListCompat.forLanguageTags("en")
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }
}
