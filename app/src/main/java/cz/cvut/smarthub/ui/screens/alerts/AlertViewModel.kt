package cz.cvut.smarthub.ui.screens.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.smarthub.database.Alert
import cz.cvut.smarthub.database.AlertRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlertViewModel(
    private val alertRepository: AlertRepository
) : ViewModel() {

    val alerts: StateFlow<List<Alert>> = alertRepository.getAllAlertsStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun insertAlert(alert: Alert) {
        viewModelScope.launch {
            alertRepository.insertAlert(alert)
        }
    }

    fun deleteAlert(alert: Alert) {
        viewModelScope.launch {
            alertRepository.deleteAlert(alert)
        }
    }

    fun deleteAllAlerts() {
        viewModelScope.launch {
            alertRepository.deleteAllAlerts()
        }
    }
}