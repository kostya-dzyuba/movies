package ru.kostyadzyuba.movies

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromText(text: String?) = text?.let { LocalDate.parse(it) }

    @TypeConverter
    fun localDateToString(localDate: LocalDate?) = localDate?.toString()
}
