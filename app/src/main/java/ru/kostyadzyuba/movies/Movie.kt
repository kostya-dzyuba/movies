package ru.kostyadzyuba.movies

import androidx.room.Entity
import java.io.Serializable
import java.time.LocalDate

@Entity(primaryKeys = ["name", "year"])
data class Movie(
    val name: String,
    val year: Short,
    val watch: LocalDate?,
    val series: Boolean
) : Serializable