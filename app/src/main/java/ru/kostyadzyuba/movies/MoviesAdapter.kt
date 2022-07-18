package ru.kostyadzyuba.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MoviesAdapter(context: Context, private val emptyView: View) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)

        private val name = itemView.findViewById<TextView>(R.id.name)
        private val year = itemView.findViewById<TextView>(R.id.year)
        private val date = itemView.findViewById<TextView>(R.id.date)

        fun bind(movie: Movie) {
            name.text = movie.name
            year.text = "${movie.year} года производства"
            date.text = "Просмотрен ${formatter.format(movie.date)}"
        }
    }

    private val dao: MovieDao
    var movies: List<Movie> private set

    init {
        val db = Room.databaseBuilder(context, AppDatabase::class.java, "movies")
            .allowMainThreadQueries()
            .build()
        dao = db.movieDao()
        movies = dao.getAll()

        if (movies.isEmpty())
            emptyView.visibility = View.VISIBLE
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
        dao.add(movie)
        (movies as MutableList<Movie>).add(movie)
        notifyItemInserted(movies.size - 1)
        emptyView.visibility = View.INVISIBLE
    }

    fun filter(query: String) {
        val old = movies
        movies = dao.filter("%$query%")

        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = old.size
            override fun getNewListSize() = movies.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                old[oldItemPosition].name == movies[newItemPosition].name

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                old[oldItemPosition] == movies[newItemPosition]
        }).dispatchUpdatesTo(this)

        emptyView.visibility =
            if (movies.isEmpty()) View.VISIBLE
            else View.INVISIBLE
    }
}
