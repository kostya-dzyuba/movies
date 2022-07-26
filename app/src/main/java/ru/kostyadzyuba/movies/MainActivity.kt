package ru.kostyadzyuba.movies

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), DialogInterface.OnClickListener {
    lateinit var adapter: MoviesAdapter
    private lateinit var initialTitle: CharSequence
    private lateinit var tabs: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialTitle = title

        tabs = findViewById(R.id.tabs)
        val emptyView = findViewById<View>(R.id.empty)
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        val add = findViewById<View>(R.id.add)

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                adapter.showSeries(tab.position == 1)
                updateTitle()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        adapter = MoviesAdapter(this, emptyView)
        recycler.adapter = adapter
        recycler.layoutManager = GridLayoutManager(this, 2)
        updateTitle()

        add.setOnClickListener {
            startActivityForResult(
                Intent(this, AddActivity::class.java)
                    .putExtra("series", tabs.selectedTabPosition == 1), REQUEST_ADD
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = "Введите название фильма"
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnSearchClickListener {
            tabs.visibility = View.GONE
            adapter.showSeries(null)
        }
        searchView.setOnCloseListener {
            tabs.visibility = View.VISIBLE
            adapter.showSeries(tabs.selectedTabPosition == 1)
            false
        }
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
            when (requestCode) {
                REQUEST_ADD -> {
                    val movie = data!!.getSerializableExtra("movie")
                    adapter.add(movie as Movie)
                    updateTitle()
                }
                REQUEST_IMPORT -> {
                    adapter.import(data!!.data!!)
                    updateTitle()
                }
                else -> throw IllegalArgumentException("requestCode")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (adapter.count == 0) onClick(null, 0) else {
            val layout = FrameLayout(this)
            val margin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
            val editText = EditText(this)
            lateinit var positive: Button
            layout.setPadding(margin, 0, margin, 0)
            editText.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            editText.hint = "Я согласен"
            editText.requestFocus()
            editText.doAfterTextChanged { positive.isEnabled = it.toString() == editText.hint }
            layout.addView(editText)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Внимание!")
                .setMessage("Список просмотренных фильмов будет очищен. Для продолжения введите: \"Я согласен\"")
                .setView(layout)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null)
                .create()
            dialog.show()
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positive.isEnabled = false
        }
        return true
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "text/*"
        startActivityForResult(intent, REQUEST_IMPORT)
    }

    private fun updateTitle() {
        if (adapter.count > 0)
            title = "$initialTitle (${adapter.count})"
    }

    companion object {
        const val REQUEST_ADD = 0
        const val REQUEST_IMPORT = 1
    }
}