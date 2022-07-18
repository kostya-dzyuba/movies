package ru.kostyadzyuba.movies

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Movie(
    @PrimaryKey
    val name: String,
    val year: Short,
    val date: LocalDate
)