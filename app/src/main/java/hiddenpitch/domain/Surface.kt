package hiddenpitch.domain

import androidx.annotation.StringRes
import hiddenpitch.R

enum class Surface(val dbValue: String, @StringRes val labelRes: Int) {
    GRASS("grass", R.string.surface_grass),
    SAND("sand", R.string.surface_sand),
    CONCRETE("concrete", R.string.surface_concrete),
    ARTIFICIAL_TURF("artificial_turf", R.string.surface_artificial_turf);

    companion object { fun fromDb(value: String): Surface? = entries.firstOrNull { it.dbValue == value } }
}
