package hiddenpitch

import androidx.lifecycle.ViewModelProvider
import hiddenpitch.ui.common.AppViewModelFactory
import hiddenpitch.ui.common.AuthGate
import hiddenpitch.util.AppConfig
import hiddenpitch.util.LocaleManager

class AppContainer {
    val localeManager = LocaleManager()
    val appConfig = AppConfig
    val authGate = AuthGate()
    val viewModelFactory: ViewModelProvider.Factory = AppViewModelFactory(localeManager)
}
