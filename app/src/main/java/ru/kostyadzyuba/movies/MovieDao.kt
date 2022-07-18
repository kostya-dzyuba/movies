package ru.kostyadzyuba.movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Query("select * from movie")
    fun getAll(): List<Movie>

    @Query("select * from movie where name like :name")
    fun filter(name: String): List<Movie>

    @Insert
    fun add(movie: Movie)
}
