package ru.kostyadzyuba.movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Query("select * from movie order by watch desc, year desc, name")
    fun getAll(): List<Movie>

    @Query("select * from movie where name like :name order by watch desc, year desc, name")
    fun filter(name: String): List<Movie>

    @Query("select * from movie where series = :value order by watch desc, year desc, name")
    fun getSeries(value: Boolean): List<Movie>

    @Query("select count(*) from movie")
    fun count(): Int

    @Insert
    fun add(movie: Movie)

    @Insert
    fun addAll(movies: List<Movie>)

    @Query("delete from movie")
    fun clear()
}
