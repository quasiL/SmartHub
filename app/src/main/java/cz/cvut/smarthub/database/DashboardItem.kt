package cz.cvut.smarthub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dashboard_items")
data class DashboardItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val value: String,
    val icon: Int
)
