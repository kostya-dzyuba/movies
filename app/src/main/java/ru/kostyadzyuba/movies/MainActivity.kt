package ru.kostyadzyuba.movies

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    lateinit var adapter: MoviesAdapter
    private lateinit var initialTitle: CharSequence

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialTitle = title

        val emptyView = findViewById<View>(R.id.empty)
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        val add = findViewById<View>(R.id.add)

        adapter = MoviesAdapter(this, emptyView)
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(this, 2)

        if (adapter.itemCount > 0)
            title = "$initialTitle (${adapter.itemCount})"

        add.setOnClickListener {
            startActivityForResult(Intent(this, AddActivity::class.java), 0)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val movie = data!!.getSerializableExtra("movie")
            adapter.add(movie as Movie)
            title = "$initialTitle (${adapter.itemCount})"
        }
    }
}