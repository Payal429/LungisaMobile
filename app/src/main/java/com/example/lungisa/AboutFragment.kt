package com.example.lungisa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AboutFragment : Fragment() {

    class AboutFragment : Fragment(R.layout.fragment_about) {

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // Find RecyclerView
            val teamRecyclerView = view.findViewById<RecyclerView>(R.id.teamRecyclerView)

            // Set layout manager
            teamRecyclerView.layoutManager = LinearLayoutManager(requireContext())

            // List of team members
            val teamMembers = listOf(
                TeamMember(R.drawable.drleahshibambo, "Dr Leah Shibambo", "Executive Director"),
                TeamMember(R.drawable.louisa, "Louisa Mokoena", "Linkage Officer/ Office Manager"),
                TeamMember(R.drawable.srjuliemakgalo, "Sr Julie Makgalo", "Professional Nurse"),
                TeamMember(R.drawable.marthamnisi, "Martha Mnisi", "Finance Officer")
            )

            // Set adapter
            val adapter = TeamAdapter(teamMembers)
            teamRecyclerView.adapter = adapter
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

}