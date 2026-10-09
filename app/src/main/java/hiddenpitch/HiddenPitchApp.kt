package hiddenpitch

import android.app.Application

class HiddenPitchApp : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer()
    }
}
