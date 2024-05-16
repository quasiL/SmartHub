package cz.cvut.smarthub.database

import kotlinx.coroutines.flow.Flow

class OfflineDashboardRepository(private val dashboardItemDao: DashboardItemDao) : DashboardRepository {
    override fun getAllDashboardItemsStream(): Flow<List<DashboardItem>> = dashboardItemDao.getAllDashboardItems()
    override suspend fun insertDashboardItems(vararg items: DashboardItem) = dashboardItemDao.insertDashboardItems(*items)
    override suspend fun deleteAllDashboardItems() = dashboardItemDao.deleteAllDashboardItems()
}