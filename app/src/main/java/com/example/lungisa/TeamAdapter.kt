package com.example.lungisa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TeamAdapter(private val teamList: List<TeamMember>) :
    RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    class TeamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivMember: ImageView = itemView.findViewById(R.id.ivTeamMember)
        val tvName: TextView = itemView.findViewById(R.id.tvTeamName)
        val tvRole: TextView = itemView.findViewById(R.id.tvTeamRole)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_team_member, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val member = teamList[position]
        holder.ivMember.setImageResource(member.imageRes)
        holder.tvName.text = member.name
        holder.tvRole.text = member.role
    }

    override fun getItemCount(): Int = teamList.size
}
