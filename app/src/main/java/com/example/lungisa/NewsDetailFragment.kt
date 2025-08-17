package com.example.lungisa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar

class NewsDetailFragment : Fragment() {

    private lateinit var newsTitle: TextView
    private lateinit var newsBody: TextView
    private lateinit var newsDate: TextView
    private lateinit var newsImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_detail, container, false)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.detailToolbar)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack() // Go back to previous fragment
        }

        newsTitle = view.findViewById(R.id.detailTitle)
        newsBody = view.findViewById(R.id.detailBody)
        newsDate = view.findViewById(R.id.detailDate)
        newsImage = view.findViewById(R.id.detailImage)

        // Receive arguments from RecyclerView click
        val args = arguments
        newsTitle.text = args?.getString("title")
        newsBody.text = args?.getString("body")
        newsDate.text = args?.getString("date")
        val imageUrl = args?.getString("imageUrl")
        imageUrl?.let {
            Glide.with(this)
                .load("https://lungisa-e03bd-default-rtdb.firebaseio.com$it")
                .placeholder(R.drawable.logolungisa)
                .into(newsImage)
        }

        return view
    }
}
