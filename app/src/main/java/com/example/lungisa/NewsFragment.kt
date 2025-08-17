package com.example.lungisa
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class NewsFragment : Fragment() {

    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var newsList: MutableList<News>
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)

        // 1️⃣ Initialize RecyclerView
        newsRecyclerView = view.findViewById(R.id.newsRecyclerView)
        newsRecyclerView.layoutManager = LinearLayoutManager(context)
        newsList = mutableListOf()

        // 2️⃣ Set adapter with click listener
        adapter = NewsAdapter(newsList) { selectedNews ->
            val bundle = Bundle().apply {
                putString("title", selectedNews.Title)
                putString("body", selectedNews.Body)
                putString("date", selectedNews.Date)
                putString("imageUrl", selectedNews.ImageUrl)
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NewsDetailFragment().apply { arguments = bundle })
                .addToBackStack(null)
                .commit()
        }
        newsRecyclerView.adapter = adapter

        // 3️⃣ Fetch news from Firebase
        fetchNews()

        return view
    }

    private fun fetchNews() {
        val database = FirebaseDatabase.getInstance()
        val newsRef = database.getReference("News")

        newsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                newsList.clear()
                for (newsSnapshot in snapshot.children) {
                    val news = newsSnapshot.getValue(News::class.java)
                    news?.let { newsList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load news: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
