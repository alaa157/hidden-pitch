package hiddenpitch.domain

import androidx.annotation.StringRes
import hiddenpitch.R

enum class Audience(val dbValue: String, @StringRes val labelRes: Int) {
    EVERYONE("everyone", R.string.audience_everyone),
    BOYS_MEN("boys_men", R.string.audience_boys_men),
    GIRLS_WOMEN("girls_women", R.string.audience_girls_women);

    companion object { fun fromDb(value: String): Audience? = entries.firstOrNull { it.dbValue == value } }
}
