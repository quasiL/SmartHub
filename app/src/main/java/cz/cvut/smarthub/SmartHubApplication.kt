package cz.cvut.smarthub

import android.app.Application
import cz.cvut.smarthub.di.AppContainer
import cz.cvut.smarthub.di.AppDataContainer

class SmartHubApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}