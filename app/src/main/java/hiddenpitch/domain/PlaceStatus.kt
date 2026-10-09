package hiddenpitch.domain

import androidx.annotation.StringRes
import hiddenpitch.R

enum class PlaceStatus(val dbValue: String, @StringRes val labelRes: Int) {
    PENDING("pending", R.string.status_pending_review),
    APPROVED("approved", R.string.status_approved),
    REJECTED("rejected", R.string.status_rejected),
    DELETED("deleted", R.string.status_deletion_pending);

    companion object { fun fromDb(value: String): PlaceStatus? = entries.firstOrNull { it.dbValue == value } }
}
