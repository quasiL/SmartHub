package cz.cvut.smarthub.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.smarthub.R
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import cz.cvut.smarthub.database.DashboardItem
import cz.cvut.smarthub.database.DashboardRepository
import kotlinx.coroutines.flow.catch

class DashboardViewModel(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    val dashboardItems: StateFlow<DashboardUiState> =
        dashboardRepository.getAllDashboardItemsStream()
            .map { items ->
                if (items.isEmpty()) {
                    addInitialDashboardItems()
                }
                DashboardUiState.Success(items)
            }
            .catch { exception -> DashboardUiState.Error(exception) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = DashboardUiState.Loading
            )

    private fun addInitialDashboardItems() {
        val initialItems = listOf(
            DashboardItem(title = "Total Devices", value = "10", icon = R.drawable.baseline_device_hub_24),
            DashboardItem(title = "Active Alerts", value = "3", icon = R.drawable.baseline_add_alert_24),
            DashboardItem(title = "Energy Usage", value = "250 kWh", icon = R.drawable.baseline_energy_savings_leaf_24),
            DashboardItem(title = "Security Status", value = "Enabled", icon = R.drawable.baseline_camera_outdoor_24)
        )
        addDashboardItems(*initialItems.toTypedArray())
    }

    fun addDashboardItems(vararg items: DashboardItem) {
        viewModelScope.launch {
            dashboardRepository.insertDashboardItems(*items)
        }
    }

    fun deleteAllDashboardItems() {
        viewModelScope.launch {
            dashboardRepository.deleteAllDashboardItems()
        }
    }
}

sealed interface DashboardUiState {
    object Loading : DashboardUiState
    data class Success(val items: List<DashboardItem>) : DashboardUiState
    data class Error(val exception: Throwable) : DashboardUiState
}