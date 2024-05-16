package cz.cvut.smarthub.database

import kotlinx.coroutines.flow.Flow

interface AlertRepository {
    fun getAllAlertsStream(): Flow<List<Alert>>
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)
    suspend fun deleteAllAlerts()
}