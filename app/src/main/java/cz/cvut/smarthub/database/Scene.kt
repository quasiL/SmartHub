package cz.cvut.smarthub.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.threeten.bp.LocalTime

@Entity(tableName = "scenes")
//@TypeConverters(Converters::class)
data class Scene(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
//    val devices: List<Long> = emptyList(),
//    val actions: List<String> = emptyList(),
    val isActive: Boolean = false,
    val startTime: LocalTime = LocalTime.now()
)
