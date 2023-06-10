package com.example.mymovis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovis.data.FavouriteMovie
import com.example.mymovis.data.MainViewModel
import com.example.mymovis.data.Movie

class FavouriteActivity : AppCompatActivity() {

    private lateinit var recyclerViewFavouryt: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        recyclerViewFavouryt = findViewById(R.id.recyclerViewFavouryt)
        recyclerViewFavouryt.layoutManager = GridLayoutManager(this, 2)
        movieAdapter = MovieAdapter()
        recyclerViewFavouryt.adapter = movieAdapter

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val favouriteMovie: LiveData<List<FavouriteMovie>> = viewModel.getFavouriteMovies()
        favouriteMovie.observe(this, Observer {
            val  movies: MutableList<Movie> = mutableListOf()
            if(it != null){
                movies.addAll(it)
                movieAdapter.setMovies(movies)
            }
        })

        movieAdapter.setOnPosterClickListener(object : MovieAdapter.OnPosterClickListener {
            override fun onPosterClick(position: Int) {
                val movie = movieAdapter.getMovies()[position]
                val intent = Intent(this@FavouriteActivity, DetailActivity::class.java)
                intent.putExtra("id", movie.id)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        when (id) {
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
}