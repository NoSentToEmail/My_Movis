package com.example.mymovis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mymovis.data.FavouriteMovie
import com.example.mymovis.data.MainViewModel
import com.example.mymovis.data.Movie
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovis.adapters.ReviewAdapter
import com.example.mymovis.adapters.TrailerAdapter
import com.example.mymovis.data.Review
import com.example.mymovis.data.Trailer
import com.example.mymovis.utils.JSONUtils
import com.example.mymovis.utils.NetworkUtils
import org.json.JSONObject


class DetailActivity : AppCompatActivity() {

    private lateinit var imageViewBigPoster: ImageView
    private lateinit var textViewLabelTitle: TextView
    private lateinit var textViewTitle: TextView
    private lateinit var textViewOriginalTitle: TextView
    private lateinit var textViewOrogonalTitle: TextView
    private lateinit var textViewRaiting: TextView
    private lateinit var textView_Raiting: TextView
    private lateinit var textViewRelisDay: TextView
    private lateinit var textViewReleaseDate: TextView
    private lateinit var textViewLabelDescription: TextView
    private lateinit var textViewOverView: TextView
    private lateinit var imageViewAddToFaivory: ImageView

    private lateinit var recycleViewTrailers: RecyclerView
    private lateinit var recyclerViewReviews: RecyclerView

    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var trailderAdapter: TrailerAdapter

    private var favouriteMovie: FavouriteMovie? = null

    private var id = 0

    private lateinit var viewModel: MainViewModel
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        imageViewBigPoster = findViewById(R.id.imageViewBigPoster)
        textViewLabelTitle = findViewById(R.id.textViewLabelTitle)
        textViewTitle = findViewById(R.id.textViewTitle)
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle)
        textViewOrogonalTitle = findViewById(R.id.textViewOrogonalTitle)
        textViewRaiting = findViewById(R.id.textViewRaiting)
        textView_Raiting = findViewById(R.id.textView_Raiting)
        textViewRelisDay = findViewById(R.id.textViewRelisDay)
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate)
        textViewLabelDescription = findViewById(R.id.textViewLabelDescription)
        textViewOverView = findViewById(R.id.textViewOverView)
        imageViewAddToFaivory = findViewById(R.id.imageViewAddToFaivory)

        val intent = intent
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1)
        } else {
            finish()
        }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        lifecycleScope.launch {
            movie = withContext(Dispatchers.IO) { viewModel.getMovieById(id) }
            Picasso.get().load(movie.bigPosterPath).into(imageViewBigPoster)
            textViewTitle.text = movie.title
            textViewOrogonalTitle.text = movie.originalTitle
            textViewOverView.text = movie.overView
            textViewReleaseDate.text = movie.releaseDate
            textView_Raiting.text = movie.voteAverage.toString()

            setFavourite()
        }

        imageViewAddToFaivory.setOnClickListener {
            viewModel.viewModelScope.launch {
                if (favouriteMovie == null) {
                    viewModel.insertFavouriteMovies(FavouriteMovie(movie))
                    Toast.makeText(this@DetailActivity, "Added to favorites", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    viewModel.deleteFavouriteMovies(favouriteMovie!!)
                    Toast.makeText(
                        this@DetailActivity,
                        "Removed from favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                setFavourite()
            }
        }
//
//        recycleViewTrailers = findViewById(R.id.recuclerViewTrailers)
//        recyclerViewReviews = findViewById(R.id.recuclerViewReviews)
//        reviewAdapter = ReviewAdapter()
//        trailderAdapter = TrailerAdapter()
//
//
//        val networkUtils = NetworkUtils()
//        val jsonUntil = JSONUtils()
//        recyclerViewReviews.layoutManager = LinearLayoutManager(this)
//        recycleViewTrailers.layoutManager = LinearLayoutManager(this)
//        recyclerViewReviews.adapter = reviewAdapter
//        recycleViewTrailers.adapter = trailderAdapter
//
//
//        val jsonObjectsTrailer: JSONObject? = networkUtils.getJSONForVideos(movie.id)
//        val jsonObjectReviews: JSONObject? = networkUtils.getJSONForReviews(movie.id)
//
//        val trailers: MutableList<Trailer>? =
//            jsonObjectsTrailer?.let { jsonUntil.getTrailerFromJSON(it) }
//        val reviews: MutableList<Review>? =
//            jsonObjectReviews?.let { jsonUntil.getReviewsFromJSON(it) }
//
//
//        reviews?.let { reviewAdapter.setReviews(it) }
//        trailers?.let { trailderAdapter.setTrailers(it) }
//
//        trailderAdapter.setOnTrailerClickListener(object :
//            TrailerAdapter.OnTrailerClickListener {
//            override fun onTrailerClick(url: String) {
//                Toast.makeText(this@DetailActivity, url, Toast.LENGTH_SHORT).show()
//            }
//        })
//

    }


    private suspend fun setFavourite() {
        favouriteMovie = viewModel.getFavouriteMovieById(id)
        if (favouriteMovie == null) {
            imageViewAddToFaivory.setImageResource(R.drawable.favourite_add_to)
        } else {
            imageViewAddToFaivory.setImageResource(R.drawable.favourite_remove)
        }
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
