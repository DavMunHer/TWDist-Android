package com.example.twdist_android.core.data.local

import androidx.room.TypeConverter
import java.time.LocalDate

/**
 * Persists calendar dates as INTEGER epoch days ([LocalDate.toEpochDay]) for correct comparisons in SQL.
 */
class TwDistRoomConverters {

    @TypeConverter
    fun localDateFromEpochDay(value: Long?): LocalDate? =
        value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun localDateToEpochDay(date: LocalDate?): Long? = date?.toEpochDay()
}
