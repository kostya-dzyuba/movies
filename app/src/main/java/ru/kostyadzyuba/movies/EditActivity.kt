package ru.kostyadzyuba.movies

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.CompoundButton
import android.widget.TextView
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var watchDate = LocalDate.now()
        var oldName: String? = null
        var oldYear: Short? = null

        val nameView = findViewById<TextView>(R.id.name)
        val yearView = findViewById<TextView>(R.id.year)
        val series = findViewById<CompoundButton>(R.id.series)
        val noDate = findViewById<CompoundButton>(R.id.no_date)
        val calendar = findViewById<CalendarView>(R.id.watch)
        val done = findViewById<View>(R.id.done)

        series.setOnCheckedChangeListener { _, isChecked ->
            nameView.hint = "Название ${if (isChecked) "сериала" else "фильма"}"
        }

        noDate.setOnCheckedChangeListener { _, isChecked ->
            calendar.visibility = if (isChecked) View.GONE else View.VISIBLE
        }

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            watchDate = LocalDate.of(year, month + 1, dayOfMonth)
        }

        done.setOnClickListener {
            val name = nameView.text.toString()
            val year = yearView.text.toString().toShortOrNull()
            if (name.isNotBlank() && name.trim().length >= 2 && year != null && year in 1900..watchDate.year) {
                val watch = if (noDate.isChecked) null else watchDate
                val movie = Movie(name.trim(), year, watch, series.isChecked)
                val intent = Intent()
                    .putExtra("name", oldName)
                    .putExtra("year", oldYear)
                    .putExtra("movie", movie)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        calendar.maxDate = TimeUnit.DAYS.toMillis(watchDate.toEpochDay())
        val extras = intent.extras!!
        val movie = extras.getSerializable("movie") as Movie?
        series.isChecked = movie?.let {
            oldName = it.name
            oldYear = it.year
            nameView.text = it.name
            yearView.text = it.year.toString()
            it.watch?.let {
                watchDate = it
                calendar.date = TimeUnit.DAYS.toMillis(it.toEpochDay())
            } ?: run { noDate.isChecked = true }
            it.series
        } ?: run {
            nameView.requestFocus()
            window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            extras.getBoolean("series")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }
}