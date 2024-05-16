package cz.cvut.smarthub.di

import android.content.Context
import cz.cvut.smarthub.database.AlertRepository
import cz.cvut.smarthub.database.AppDatabase
import cz.cvut.smarthub.database.DashboardRepository
import cz.cvut.smarthub.database.DeviceRepository
import cz.cvut.smarthub.database.SceneRepository
import cz.cvut.smarthub.database.OfflineAlertRepository
import cz.cvut.smarthub.database.OfflineDashboardRepository
import cz.cvut.smarthub.database.OfflineDeviceRepository
import cz.cvut.smarthub.database.OfflineSceneRepository

interface AppContainer {
    val deviceRepository: DeviceRepository
    val dashboardRepository: DashboardRepository
    val alertRepository: AlertRepository
    val sceneRepository: SceneRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val deviceRepository: DeviceRepository by lazy {
        OfflineDeviceRepository(AppDatabase.getDatabase(context).deviceDao())
    }

    override val dashboardRepository: DashboardRepository by lazy {
        OfflineDashboardRepository(AppDatabase.getDatabase(context).dashboardItemDao())
    }

    override val alertRepository: AlertRepository by lazy {
        OfflineAlertRepository(AppDatabase.getDatabase(context).alertDao())
    }

    override val sceneRepository: SceneRepository by lazy {
        OfflineSceneRepository(AppDatabase.getDatabase(context).sceneDao())
    }
}