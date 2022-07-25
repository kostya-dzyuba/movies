package ru.kostyadzyuba.movies

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.WindowManager
import android.widget.CalendarView
import android.widget.CompoundButton
import android.widget.TextView
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        var watchDate = LocalDate.now()

        val nameView = findViewById<TextView>(R.id.name)
        val yearView = findViewById<TextView>(R.id.year)
        val series = findViewById<CompoundButton>(R.id.series)
        val noDate = findViewById<CompoundButton>(R.id.no_date)
        val calendar = findViewById<CalendarView>(R.id.watch)
        val done = findViewById<View>(R.id.done)

        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        nameView.requestFocus()
        yearView.filters = arrayOf(InputFilter.LengthFilter(4))
        calendar.maxDate = TimeUnit.DAYS.toMillis(watchDate.toEpochDay())

        series.setOnCheckedChangeListener { _, isChecked ->
            nameView.hint = "Название ${if (isChecked) "сериала" else "фильма"}"
        }

        series.isChecked = intent.extras!!.getBoolean("series")

        noDate.setOnCheckedChangeListener { _, isChecked ->
            calendar.visibility = if (isChecked) View.GONE else View.VISIBLE
        }

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            watchDate = LocalDate.of(year, month, dayOfMonth)
        }

        done.setOnClickListener {
            val name = nameView.text.toString()
            val year = yearView.text.toString().toShortOrNull()

            if (name.isNotBlank() && name.trim().length >= 2 && year != null && year in 1900..watchDate.year) {
                val watch = if (noDate.isChecked) null else watchDate
                val movie = Movie(name.trim(), year, watch, series.isChecked)
                setResult(Activity.RESULT_OK, Intent().putExtra("movie", movie))
                finish()
            }
        }
    }
}