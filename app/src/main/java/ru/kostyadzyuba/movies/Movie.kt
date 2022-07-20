package ru.kostyadzyuba.movies

import androidx.room.Entity
import java.time.LocalDate

@Entity(primaryKeys = ["name", "year"])
data class Movie(
    val name: String,
    val year: Short,
    val date: LocalDate?
)