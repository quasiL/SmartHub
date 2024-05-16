package cz.cvut.smarthub.ui.utils

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cz.cvut.smarthub.SmartHubApplication
import cz.cvut.smarthub.ui.screens.alerts.AlertViewModel
import cz.cvut.smarthub.ui.screens.devices.DeviceDetailViewModel
import cz.cvut.smarthub.ui.screens.devices.DevicesViewModel
import cz.cvut.smarthub.ui.screens.dashboard.DashboardViewModel
import cz.cvut.smarthub.ui.screens.scenes.ScenesViewModel
import cz.cvut.smarthub.ui.screens.scenes.SceneDetailViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            DevicesViewModel(
                smartHubApplication().container.deviceRepository
            )
        }
        initializer {
            DeviceDetailViewModel(
                smartHubApplication().container.deviceRepository,
                this.createSavedStateHandle()
            )
        }
        initializer {
            DashboardViewModel(
                smartHubApplication().container.dashboardRepository
            )
        }
        initializer {
            AlertViewModel(
                smartHubApplication().container.alertRepository
            )
        }
        initializer {
            ScenesViewModel(
                smartHubApplication().container.sceneRepository
            )
        }
        initializer {
            SceneDetailViewModel(
                smartHubApplication().container.sceneRepository,
                this.createSavedStateHandle()
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.smartHubApplication(): SmartHubApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SmartHubApplication)