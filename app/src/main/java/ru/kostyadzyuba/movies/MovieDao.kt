package ru.kostyadzyuba.movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Query("select * from OrderedMovie")
    fun getAll(): List<Movie>

    @Query("select * from OrderedMovie where name like :name")
    fun filter(name: String): List<Movie>

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

    @Query("delete from Movie")
    fun clear()
}
