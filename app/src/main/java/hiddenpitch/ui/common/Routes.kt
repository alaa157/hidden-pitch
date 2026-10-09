package hiddenpitch.ui.common

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{placeId}"
    const val ADD_PLACE = "add-place"
    const val MY_PLACES = "my-places"
    const val SETTINGS = "settings"

    fun detail(placeId: String): String = "detail/$placeId"
}
