package com.example.lungisa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AboutFragment : Fragment(R.layout.fragment_about) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the RecyclerView
        val teamRecyclerView = view.findViewById<RecyclerView>(R.id.teamRecyclerView)

        // Set layout manager
        teamRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Get the list of team members from TeamData
        val teamMembers = TeamData.teamMembers

        // Set adapter
        val adapter = TeamAdapter(teamMembers)
        teamRecyclerView.adapter = adapter
    }
}


