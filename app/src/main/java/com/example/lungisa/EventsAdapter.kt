package com.example.lungisa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventsAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvEventName)
        val tvDescription: TextView = itemView.findViewById(R.id.tvEventDescription)
        val tvEventDate: TextView = itemView.findViewById(R.id.tvEventDate)
        val tvEventTime: TextView = itemView.findViewById(R.id.tvEventTime)
        val tvVenue: TextView = itemView.findViewById(R.id.tvEventVenue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.tvName.text = event.Name
        holder.tvDescription.text = event.Description

        // Split DateTime into date and time
        event.DateTime?.let {
            val parts = it.split("T") // "2025-08-15T13:40"
            if (parts.size == 2) {
                holder.tvEventDate.text = parts[0] // date
                holder.tvEventTime.text = parts[1] // time
            } else {
                holder.tvEventDate.text = it
                holder.tvEventTime.text = ""
            }
        }

        holder.tvVenue.text = event.Venue
    }


    override fun getItemCount(): Int = events.size
}
