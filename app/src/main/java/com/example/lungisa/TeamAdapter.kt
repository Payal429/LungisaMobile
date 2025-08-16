package com.example.lungisa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TeamAdapter(private val members: List<TeamMember>) :
    RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val memberImage: ImageView = itemView.findViewById(R.id.memberImage)
        val memberInfo: TextView = itemView.findViewById(R.id.memberInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_member_item, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val member = members[position]
        holder.memberImage.setImageResource(member.imageRes)
        holder.memberInfo.text =
            "${member.name}\n${member.role}"
    }

    override fun getItemCount(): Int = members.size
}
