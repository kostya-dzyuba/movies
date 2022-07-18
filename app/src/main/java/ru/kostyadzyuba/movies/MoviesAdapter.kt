package ru.kostyadzyuba.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MoviesAdapter(var movies: List<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.name)
        private val year = itemView.findViewById<TextView>(R.id.year)
        private val date = itemView.findViewById<TextView>(R.id.date)

        fun bind(movie: Movie) {
            name.text = movie.name
            year.text = "${movie.year} года производства"
            date.text = "Просмотрен ${movie.date}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(movies[position])

    override fun getItemCount() = movies.size

    fun add(movie: Movie) {
        (movies as MutableList).add(movie)
        notifyItemInserted(movies.size - 1)
    }
}
