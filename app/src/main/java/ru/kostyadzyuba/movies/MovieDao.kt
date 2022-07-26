package ru.kostyadzyuba.movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate

@Dao
interface MovieDao {
    @Query("select * from OrderedMovie")
    fun getAll(): List<Movie>

    @Query("select * from OrderedMovie where name like :name")
    fun filter(name: String): List<Movie>

    @Query("select * from OrderedMovie where name like :name and series = :series")
    fun filter(name: String, series: Boolean): List<Movie>

    @Query("select * from OrderedMovie where series = :value")
    fun getSeries(value: Boolean): List<Movie>

    @Query("select count(*) from Movie")
    fun count(): Int

    @Query("select count(*) from Movie where series = :series")
    fun count(series: Boolean): Int

    @Insert
    fun add(movie: Movie)

    @Insert
    fun addAll(movies: List<Movie>)

    @Query("update Movie set name = :name, year = :year, watch = :watch, " +
            "series = :series where name = :oldName and year = :oldYear")
    fun edit(
        oldName: String, oldYear: Short, name: String,
        year: Short, watch: LocalDate?, series: Boolean
    )

    @Query("delete from Movie")
    fun clear()
}
