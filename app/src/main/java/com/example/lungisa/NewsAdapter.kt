package com.example.lungisa
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class NewsAdapter(
    private val newsList: List<News>,
    private val onItemClick: (News) -> Unit   // Click listener for opening detail
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.newsTitle)
        val summary: TextView = itemView.findViewById(R.id.newsSummary)
        val date: TextView = itemView.findViewById(R.id.newsDate)
        val image: ImageView = itemView.findViewById(R.id.newsImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]

        // Set news data
        holder.title.text = news.Title
        holder.summary.text = news.Summary
        holder.date.text = news.Date

        // Load image using Glide
        val imageUrl = "https://lungisa-e03bd-default-rtdb.firebaseio.com${news.ImageUrl}"
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_news)
            .into(holder.image)

        // Click listener: call the lambda passed from fragment
        holder.itemView.setOnClickListener {
            onItemClick(news)
        }
    }

    override fun getItemCount(): Int = newsList.size
}



