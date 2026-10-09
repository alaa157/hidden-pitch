package hiddenpitch.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hiddenpitch.ui.add.AddPlaceViewModel
import hiddenpitch.ui.detail.DetailViewModel
import hiddenpitch.ui.home.HomeViewModel
import hiddenpitch.ui.myplaces.MyPlacesViewModel
import hiddenpitch.ui.settings.SettingsViewModel
import hiddenpitch.util.LocaleManager

class AppViewModelFactory(private val localeManager: LocaleManager) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        HomeViewModel::class.java -> HomeViewModel()
        DetailViewModel::class.java -> DetailViewModel()
        AddPlaceViewModel::class.java -> AddPlaceViewModel()
        MyPlacesViewModel::class.java -> MyPlacesViewModel()
        SettingsViewModel::class.java -> SettingsViewModel(localeManager)
        else -> error("Unknown ViewModel class: " + modelClass.name)
    } as T
}
