package cz.cvut.smarthub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val type: String = "",
    val description: String = "",
    val location: String = "",
    val ipAddress: String = ""
)