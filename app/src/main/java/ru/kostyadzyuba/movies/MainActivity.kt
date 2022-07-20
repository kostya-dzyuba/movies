package ru.kostyadzyuba.movies

import android.os.Bundle
import android.text.InputFilter
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.ZoneOffset

class MainActivity : AppCompatActivity() {
    lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emptyView = findViewById<TextView>(R.id.empty)
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        val add = findViewById<FloatingActionButton>(R.id.add)

        adapter = MoviesAdapter(this, emptyView)
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(this, 2)

        val initialTitle = title
        if (adapter.itemCount > 0)
            title = "$initialTitle (${adapter.itemCount})"

        add.setOnClickListener {
            val today = LocalDate.now()
            val root = layoutInflater.inflate(R.layout.dialog_add, null)

            val nameView = root.findViewById<TextView>(R.id.name)
            val yearView = root.findViewById<TextView>(R.id.year)
            val todayView = root.findViewById<CompoundButton>(R.id.today)
            val datePicker = root.findViewById<DatePicker>(R.id.date)

            nameView.requestFocus()
            yearView.filters = arrayOf(InputFilter.LengthFilter(4))
            datePicker.maxDate = today.minusDays(1)
                .atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

            todayView.setOnCheckedChangeListener { _, isChecked ->
                datePicker.visibility = if (isChecked) View.GONE else View.VISIBLE
            }

            val dialog = AlertDialog.Builder(this)
                .setTitle("Добавить фильм")
                .setView(root)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create()

            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            dialog.show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val name = nameView.text.toString()
                val year = yearView.text.toString().toShortOrNull()

                if (name.isNotBlank() && name.length >= 2 && year != null &&
                    year in 1900..today.year && !adapter.has(name)
                ) {
                    val date = if (todayView.isChecked) today else
                        LocalDate.of(datePicker.year, datePicker.month, datePicker.dayOfMonth)
                    val movie = Movie(name, year, date)
                    adapter.add(movie)
                    title = "$initialTitle (${adapter.itemCount})"
                    dialog.dismiss()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = "Введите название фильма"
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String) = false
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText)
                return true
            }
        })
        return true
    }
}