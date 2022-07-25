package ru.kostyadzyuba.movies

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.opencsv.CSVReaderHeaderAware
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MoviesAdapter(private val context: Context, private val emptyView: View) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)

        private val name = itemView.findViewById<TextView>(R.id.name)
        private val year = itemView.findViewById<TextView>(R.id.year)
        private val watch = itemView.findViewById<TextView>(R.id.watch)

        fun bind(movie: Movie) {
            name.text = movie.name
            year.text = "${movie.year} года производства"

            watch.visibility = movie.watch?.let {
                watch.text = "Просмотр ${formatter.format(movie.watch)}"
                View.VISIBLE
            } ?: View.GONE
        }
    }

    private val dao: MovieDao
    private var movies: List<Movie>
    private var seriesShown: Boolean? = false
    val count get() = dao.count()

    init {
        val db = Room.databaseBuilder(context, AppDatabase::class.java, "movies")
            .allowMainThreadQueries()
            .build()
        dao = db.movieDao()
        movies = dao.getSeries(false)

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
        diff(dao.filter("%$query%"))
    }

    fun import(uri: Uri) {
        val stream = context.contentResolver.openInputStream(uri)
        val reader = CSVReaderHeaderAware(InputStreamReader(stream))
        val imported = reader.map {
            val watch = if (it[2] == "NULL") null else LocalDate.parse(it[2])
            Movie(it[0], it[1].toShort(), watch, it[3].toInt() == 1)
        }
        dao.clear()
        dao.addAll(imported)
        diff(dao.getSeries(seriesShown!!))
    }

    fun showSeries(showSeries: Boolean?) {
        seriesShown = showSeries
        diff(showSeries?.let { dao.getSeries(it) } ?: dao.getAll())
    }

    private fun diff(new: List<Movie>) {
        val old = movies
        movies = new
        DiffUtil.calculateDiff(MoviesDiff(old, new)).dispatchUpdatesTo(this)
        emptyView.visibility =
            if (new.isEmpty()) View.VISIBLE
            else View.INVISIBLE
    }
}
