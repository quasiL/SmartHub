package cz.cvut.smarthub.database

import androidx.room.TypeConverter
import org.threeten.bp.LocalTime
import java.time.format.DateTimeFormatter

object Converters {

    @TypeConverter
    fun fromLongList(value: String): List<Long> {
        return value.split(",").map { it.toLong() }
    }

    @TypeConverter
    fun toLongList(list: List<Long>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun fromStringList(value: String): List<String> {
        return value.split(",")
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    @JvmStatic
    fun fromString(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    @JvmStatic
    fun toString(time: LocalTime?): String? {
        return time?.toString()
    }
}