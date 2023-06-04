package com.example.mymovis

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovis.utils.JSONUtils
import com.example.mymovis.utils.NetworkUtils
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var recyclerViewPoster: RecyclerView
    private lateinit var switchSort: Switch
    private lateinit var textViewPopularity: TextView
    private lateinit var textViewTopRated:TextView

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
        textViewPopularity = findViewById(R.id.textViewPopularity)
        textViewTopRated = findViewById(R.id.textViewTopRated)
        recyclerViewPoster = findViewById(R.id.recyclerViewPoster)
        recyclerViewPoster.layoutManager = GridLayoutManager(this, 3)
        movieAdapter = MovieAdapter()
        recyclerViewPoster.adapter = movieAdapter

        switchSort = findViewById(R.id.switchSort)
        switchSort.isChecked = true
        switchSort.setOnCheckedChangeListener { buttonView, isChecked ->
            setMethodOfSort(isChecked)
        }
        switchSort.isChecked = false

        textViewTopRated.setOnClickListener(){
            setMethodOfSort(true)
            switchSort.isChecked = true
        }
        textViewPopularity.setOnClickListener(){
            setMethodOfSort(false)
            switchSort.isChecked = false
        }
        movieAdapter.setOnPosterClickListener(posterClickListener)
        movieAdapter.setOnReachEndListener(onReachEndListener)



    }

    private fun setMethodOfSort(isTopRated: Boolean){
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

        val jsonObject = NetworkUtils().getJSONFromNetwork(methodOfSort, 2)
        val movies = jsonObject?.let { JSONUtils().getMoviesFromJSON(it) }
        movies?.let { movieAdapter.addMovies(it) }
    }
}