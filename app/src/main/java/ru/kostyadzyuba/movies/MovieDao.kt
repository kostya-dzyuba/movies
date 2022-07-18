package ru.kostyadzyuba.movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MovieDao {
    @Query("select * from movie")
    fun getAll(): List<Movie>

    @Insert
    fun add(movie: Movie)
}
