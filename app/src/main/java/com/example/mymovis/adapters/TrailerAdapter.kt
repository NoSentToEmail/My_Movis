package com.example.mymovis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovis.R
import com.example.mymovis.data.Review
import com.example.mymovis.data.Trailer

class TrailerAdapter: RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {

    private var trailers: MutableList<Trailer> = mutableListOf()
    private lateinit var onTrailerClickListener: OnTrailerClickListener

    interface OnTrailerClickListener {
        fun onTrailerClick(url: String)
    }

    inner class TrailerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNameOfVideo: TextView = itemView.findViewById(R.id.textViewNameOfVideo)

        init {
            itemView.setOnClickListener {
                if (onTrailerClickListener != null) {
                    onTrailerClickListener.onTrailerClick(trailers[adapterPosition].getKey())
                }
            }
        }
    }

    fun setOnTrailerClickListener(onTrailerClickListener: OnTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener
    }

    fun setTrailers(trailer: MutableList<Trailer>) {
        this.trailers = trailer
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.trailer_item, parent, false)
        return TrailerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trailers.size
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val trailer = trailers[position]
        holder.textViewNameOfVideo.text = trailer.getName()
    }
}
