package cz.cvut.smarthub.database

import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getAllDashboardItemsStream(): Flow<List<DashboardItem>>
    suspend fun insertDashboardItems(vararg items: DashboardItem)
    suspend fun deleteAllDashboardItems()
}