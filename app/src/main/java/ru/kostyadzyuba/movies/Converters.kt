package ru.kostyadzyuba.movies

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromEpochDay(epochDay: Short): LocalDate = LocalDate.ofEpochDay(epochDay.toLong())

    @TypeConverter
    fun localDateToEpochDay(localDate: LocalDate) = localDate.toEpochDay().toShort()
}
