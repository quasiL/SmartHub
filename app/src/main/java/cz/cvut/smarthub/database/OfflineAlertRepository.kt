package cz.cvut.smarthub.database

import kotlinx.coroutines.flow.Flow

class OfflineAlertRepository(private val alertDao: AlertDao) : AlertRepository {
    override fun getAllAlertsStream(): Flow<List<Alert>> = alertDao.getAllAlerts()
    override suspend fun insertAlert(alert: Alert) = alertDao.insertAlert(alert)
    override suspend fun deleteAlert(alert: Alert) = alertDao.delete(alert)
    override suspend fun deleteAllAlerts() = alertDao.deleteAllAlerts()
}