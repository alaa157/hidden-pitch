package hiddenpitch.domain

import androidx.annotation.StringRes
import hiddenpitch.R

enum class Sport(val dbValue: String, @StringRes val labelRes: Int) {
    FOOTBALL("football", R.string.sport_football),
    BASKETBALL("basketball", R.string.sport_basketball),
    VOLLEYBALL("volleyball", R.string.sport_volleyball),
    TENNIS("tennis", R.string.sport_tennis),
    PADEL("padel", R.string.sport_padel),
    PLAYGROUND("playground", R.string.sport_playground),
    OTHER("other", R.string.sport_other);

    companion object { fun fromDb(value: String): Sport? = entries.firstOrNull { it.dbValue == value } }
}
