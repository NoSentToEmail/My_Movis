package com.example.mymovis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovis.R
import com.example.mymovis.data.Movie
import com.example.mymovis.data.Review

class ReviewAdapter: RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var reviews: MutableList<Review> = mutableListOf()

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewAuthor: TextView = itemView.findViewById(R.id.textViewAuthor)
        val textViewContent: TextView = itemView.findViewById(R.id.textViewContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.review_item, parent, false)
        return ReviewViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.textViewContent.text = review.getContent()
        holder.textViewAuthor.text = review.getAuthor()
    }

    fun setReviews(reviews: MutableList<Review>){
        this.reviews = reviews
        notifyDataSetChanged()
    }
}