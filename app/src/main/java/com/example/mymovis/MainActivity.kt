package com.example.mymovis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovis.adapters.MovieAdapter
import com.example.mymovis.data.MainViewModel
import com.example.mymovis.data.Movie
import com.example.mymovis.utils.JSONUtils
import com.example.mymovis.utils.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject


class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<JSONObject> {
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var recyclerViewPoster: RecyclerView
    private lateinit var switchSort: Switch
    private lateinit var textViewPopularity: TextView
    private lateinit var textViewTopRated: TextView
    private lateinit var progressBarLoading: ProgressBar

    private var jsonData: JSONObject? = null // Добавьте переменную для хранения данных


    private val LOADER_ID = 124
    private lateinit var loaderManager: LoaderManager

    private var isLoading = false

    private var page: Int = 1
    private var methodOfSort = 0

    private lateinit var viewModel: MainViewModel

    private val posterClickListener = object : MovieAdapter.OnPosterClickListener {
        override fun onPosterClick(position: Int) {
            Toast.makeText(this@MainActivity, "Clicked: $position", Toast.LENGTH_SHORT).show()
        }
    }

    private val onReachEndListener = object : MovieAdapter.OnReachEndListener {
        override fun onReachEnd() {
            if (!isLoading) {
                isLoading = true
                downloadData(methodOfSort, page)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loaderManager = LoaderManager.getInstance(this)
        openOptionsMenu()


        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        textViewPopularity = findViewById(R.id.textViewPopularity)
        textViewTopRated = findViewById(R.id.textViewTopRated)
        recyclerViewPoster = findViewById(R.id.recyclerViewPoster)
        progressBarLoading = findViewById(R.id.progressBarLoading)
        recyclerViewPoster.layoutManager = GridLayoutManager(this, 2)
        movieAdapter = MovieAdapter()
        recyclerViewPoster.adapter = movieAdapter

        switchSort = findViewById(R.id.switchSort)
        switchSort.isChecked = true
        switchSort.setOnCheckedChangeListener { buttonView, isChecked ->
            page = 1
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
            if (page == 1) {
                movieAdapter.setMovies(movies as MutableList<Movie>)
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
        if (isTopRated) {
            textViewTopRated.setTextColor(textViewPopularity.context.resources.getColor(R.color.бирюзовый))
            textViewPopularity.setTextColor(textViewPopularity.context.resources.getColor(R.color.white))
            methodOfSort = NetworkUtils().TOP_RATED
        } else {
            textViewPopularity.setTextColor(textViewPopularity.context.resources.getColor(R.color.бирюзовый))
            textViewTopRated.setTextColor(textViewPopularity.context.resources.getColor(R.color.white))
            methodOfSort = NetworkUtils().POPULARITY
        }
        downloadData(methodOfSort, page)
    }

    private fun downloadData(methodOfSort: Int, page: Int) {
        val networkUtils = NetworkUtils()
        val url = networkUtils.buildURL(methodOfSort, page)
        val bundle = Bundle()
        bundle.putString("url", url.toString())
        loaderManager.restartLoader(LOADER_ID, bundle, this)

        isLoading = false
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<JSONObject> {
        val jsonLoader = NetworkUtils.JSONLoader(this, args)
        jsonLoader.setOnStartLoadingListener(object :
            NetworkUtils.JSONLoader.OnStartLoadingListener {
            override fun onStartLoading() {
                progressBarLoading.visibility =
                    View.VISIBLE // Для установки видимости прогресс-бара
                    isLoading = true

                // Дополнительный код, выполняющийся при начале загрузки
                // Например, обновление пользовательского интерфейса или отображение индикатора загрузки
            }
        })
        return jsonLoader
    }

    override fun onLoaderReset(loader: Loader<JSONObject>) {
        val movies = jsonData?.let { JSONUtils().getMoviesFromJSON(it) }
        if (movies != null && movies.isNotEmpty()) {
            viewModel.deleteAllMovies()
            for (movie in movies) {
                viewModel.insert(movie)
            }
        }
        loaderManager.destroyLoader(LOADER_ID)
    }

    override fun onLoadFinished(loader: Loader<JSONObject>, data: JSONObject?) {
        if (data != null) {
            jsonData = data // Сохраняем данные в переменную jsonData
            val movies = JSONUtils().getMoviesFromJSON(data)
            if (movies != null && movies.isNotEmpty()) {
                if (page == 1) {
                    viewModel.deleteAllMovies()
                    movieAdapter.clear()
                }
                for (movie in movies) {
                    viewModel.insert(movie)
                }
                movieAdapter.setMovies(movies as MutableList<Movie>)
                recyclerViewPoster.scrollToPosition(0)

            }
        }
        loaderManager.destroyLoader(LOADER_ID)
        isLoading = false
        progressBarLoading.visibility = View.INVISIBLE // Для установки невидимости прогресс-бара
        page++



    }
}
