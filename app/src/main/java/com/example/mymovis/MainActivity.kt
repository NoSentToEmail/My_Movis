package com.example.mymovis

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovis.MovieAdapter
import com.example.mymovis.R
import com.example.mymovis.data.MainViewModel
import com.example.mymovis.data.Movie
import com.example.mymovis.utils.JSONUtils
import com.example.mymovis.utils.NetworkUtils
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var recyclerViewPoster: RecyclerView
    private lateinit var switchSort: Switch
    private lateinit var textViewPopularity: TextView
    private lateinit var textViewTopRated: TextView

    private lateinit var viewModel: MainViewModel

    private val posterClickListener = object : MovieAdapter.OnPosterClickListener {
        override fun onPosterClick(position: Int) {
            Toast.makeText(this@MainActivity, "Clicked: $position", Toast.LENGTH_SHORT).show()
        }
    }

    private val onReachEndListener = object : MovieAdapter.OnReachEndListener {
        override fun onReachEnd() {
            Toast.makeText(this@MainActivity, "Конец списка", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openOptionsMenu()


        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        textViewPopularity = findViewById(R.id.textViewPopularity)
        textViewTopRated = findViewById(R.id.textViewTopRated)
        recyclerViewPoster = findViewById(R.id.recyclerViewPoster)
        recyclerViewPoster.layoutManager = GridLayoutManager(this, 2)
        movieAdapter = MovieAdapter()
        recyclerViewPoster.adapter = movieAdapter

        switchSort = findViewById(R.id.switchSort)
        switchSort.isChecked = true
        switchSort.setOnCheckedChangeListener { buttonView, isChecked ->
            setMethodOfSort(isChecked)
        }
        switchSort.isChecked = false

        textViewTopRated.setOnClickListener {
            setMethodOfSort(true)
            switchSort.isChecked = true
        }
        textViewPopularity.setOnClickListener {
            setMethodOfSort(false)
            switchSort.isChecked = false
        }

        movieAdapter.setOnPosterClickListener(posterClickListener)
        movieAdapter.setOnReachEndListener(onReachEndListener)

        movieAdapter.setOnPosterClickListener(object : MovieAdapter.OnPosterClickListener {
            override fun onPosterClick(position: Int) {
                val movie = movieAdapter.getMovies()[position]
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("id", movie.id)
                startActivity(intent)
            }
        })


        viewModel.getMovies().observe(this, Observer { movies ->
            if (movies != null) {
                Log.d("MainActivity", "Movies loaded: ${movies.size}")
                movieAdapter.setMovies(movies as MutableList<Movie>)
                recyclerViewPoster.scrollToPosition(0)
            } else {
                Log.e("MainActivity", "Movies list is null")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemMain -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.itemFavourite -> {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setMethodOfSort(isTopRated: Boolean) {
        val methodOfSort: Int
        if (isTopRated) {
            textViewTopRated.setTextColor(textViewPopularity.context.resources.getColor(R.color.бирюзовый))
            textViewPopularity.setTextColor(textViewPopularity.context.resources.getColor(R.color.white))
            methodOfSort = NetworkUtils().TOP_RATED
        } else {
            textViewPopularity.setTextColor(textViewPopularity.context.resources.getColor(R.color.бирюзовый))
            textViewTopRated.setTextColor(textViewPopularity.context.resources.getColor(R.color.white))
            methodOfSort = NetworkUtils().POPULARITY
        }
        downloadData(methodOfSort, 1)
    }

    private fun downloadData(methodOfSort: Int, page: Int) {
        val jsonObject = NetworkUtils().getJSONFromNetwork(methodOfSort, page)
        val movies = jsonObject?.let { JSONUtils().getMoviesFromJSON(it) }
        if (movies != null && movies.isNotEmpty()) {
            viewModel.deleteAllMovies()
            for (movie in movies) {
                viewModel.insert(movie)
            }
            Log.d("MainActivity", "Movies downloaded: ${movies.size}")
        } else {
            Log.e("MainActivity", "Failed to parse JSON or empty movie list")
        }
    }

}
