package ru.kostyadzyuba.movies

import androidx.room.DatabaseView
import androidx.room.Entity
import java.io.Serializable
import java.time.LocalDate

@Entity(primaryKeys = ["name", "year"])
@DatabaseView("select * from Movie order by watch desc, year desc, name", viewName = "OrderedMovie")
data class Movie(
    val name: String,
    val year: Short,
    val watch: LocalDate?,
    val series: Boolean
) : Serializable