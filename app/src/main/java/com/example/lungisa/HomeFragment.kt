package com.example.lungisa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.android.material.button.MaterialButton

class HomeFragment : Fragment() {

    private lateinit var database: DatabaseReference

    // Project TextViews
    private lateinit var tvCompletedTitle: TextView
    private lateinit var tvCompletedDescription: TextView
    private lateinit var tvCompletedDates: TextView

    private lateinit var tvCurrentTitle: TextView
    private lateinit var tvCurrentDescription: TextView
    private lateinit var tvCurrentDates: TextView

    private lateinit var tvUpcomingTitle: TextView
    private lateinit var tvUpcomingDescription: TextView
    private lateinit var tvUpcomingDates: TextView

    // Events RecyclerView
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter

    // Newsletter subscription
    private lateinit var subscribeEmail: EditText
    private lateinit var subscribeButton: MaterialButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize Firebase database reference
        database =
            FirebaseDatabase.getInstance("https://lungisa-e03bd-default-rtdb.firebaseio.com/")
                .getReference("Projects")

        // --- Initialize Project TextViews ---
        tvCompletedTitle = view.findViewById(R.id.tv_completed_title)
        tvCompletedDescription = view.findViewById(R.id.tv_completed_description)
        tvCompletedDates = view.findViewById(R.id.tv_completed_dates)

        tvCurrentTitle = view.findViewById(R.id.tv_current_title)
        tvCurrentDescription = view.findViewById(R.id.tv_current_description)
        tvCurrentDates = view.findViewById(R.id.tv_current_dates)

        tvUpcomingTitle = view.findViewById(R.id.tv_upcoming_title)
        tvUpcomingDescription = view.findViewById(R.id.tv_upcoming_description)
        tvUpcomingDates = view.findViewById(R.id.tv_upcoming_dates)


        // --- Initialize Events RecyclerView ---
        eventsRecyclerView = view.findViewById(R.id.eventsRecyclerView)
        eventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // --- Initialize newsletter subscription views ---
        subscribeEmail = view.findViewById(R.id.input_subscribe_email)
        subscribeButton = view.findViewById(R.id.subscribeButton)
        subscribeButton.setOnClickListener {
            val email = subscribeEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                saveSubscriberToFirebase(email)
            }
        }

        // Volunteer button
        val volunteerButton: MaterialButton = view.findViewById(R.id.volunteerButton)
        volunteerButton.setOnClickListener {
            // Replace current fragment with VolunteerFragment
            val volunteerFragment = VolunteerFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, volunteerFragment) // fragment_container is the ID of your FrameLayout or container
                .addToBackStack(null)
                .commit()
        }


        // --- Load Data ---
        loadLatestProjects()
        loadLatestEvents()

        return view
    }

    // --- Load Latest Project per Type ---
    private fun loadLatestProjects() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var latestCompleted: Project? = null
                var latestCurrent: Project? = null
                var latestUpcoming: Project? = null

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                for (projectSnapshot in snapshot.children) {
                    val project = projectSnapshot.getValue(Project::class.java) ?: continue

                    // Parse the StartDate
                    val projectStartDate = project.StartDate?.let { sdf.parse(it) }

                    when (project.Type?.lowercase()) {
                        "completed" -> {
                            val latestDate = latestCompleted?.StartDate?.let { sdf.parse(it) }
                            if (latestCompleted == null || (projectStartDate != null && projectStartDate.after(latestDate))) {
                                latestCompleted = project
                            }
                        }
                        "currently" -> {
                            val latestDate = latestCurrent?.StartDate?.let { sdf.parse(it) }
                            if (latestCurrent == null || (projectStartDate != null && projectStartDate.after(latestDate))) {
                                latestCurrent = project
                            }
                        }
                        "upcoming" -> {
                            val latestDate = latestUpcoming?.StartDate?.let { sdf.parse(it) }
                            if (latestUpcoming == null || (projectStartDate != null && projectStartDate.after(latestDate))) {
                                latestUpcoming = project
                            }
                        }
                    }
                }

                // Update UI
                latestCompleted?.let {
                    tvCompletedTitle.text = it.Title
                    tvCompletedDescription.text = it.Description
                    tvCompletedDates.text = "Start: ${it.StartDate}  End: ${it.EndDate}"
                }

                latestCurrent?.let {
                    tvCurrentTitle.text = it.Title
                    tvCurrentDescription.text = it.Description
                    tvCurrentDates.text = "Start: ${it.StartDate}  End: ${it.EndDate}"
                }

                latestUpcoming?.let {
                    tvUpcomingTitle.text = it.Title
                    tvUpcomingDescription.text = it.Description
                    tvUpcomingDates.text = "Start: ${it.StartDate}  End: ${it.EndDate}"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load projects", Toast.LENGTH_SHORT).show()
            }
        })
    }
    // --- Load Latest 3 Events ---
    private fun loadLatestEvents() {
        val eventsDatabase = FirebaseDatabase.getInstance("https://lungisa-e03bd-default-rtdb.firebaseio.com/")
            .getReference("Events")

        eventsDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val eventsList = mutableListOf<Event>()
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())

                for (eventSnapshot in snapshot.children) {
                    val event = eventSnapshot.getValue(Event::class.java) ?: continue
                    eventsList.add(event)
                }

                // Sort by DateTime descending and take latest 3
                val latestEvents = eventsList.sortedByDescending {
                    it.DateTime?.let { dt -> sdf.parse(dt) }
                }.take(3)

                eventsAdapter = EventsAdapter(latestEvents)
                eventsRecyclerView.adapter = eventsAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load events", Toast.LENGTH_SHORT).show()
            }
        })
    }
    // --- Save subscriber and send email using EmailSender.kt ---
    private fun saveSubscriberToFirebase(email: String) {
        val database = FirebaseDatabase.getInstance()
        val subscribersRef = database.getReference("Subscribers")

        val currentDateTime = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        val subscriber = Subscriber(email, currentDateTime)

        subscribersRef.push().setValue(subscriber)
            .addOnSuccessListener {
                Toast.makeText(context, "Subscribed successfully!", Toast.LENGTH_SHORT).show()
                subscribeEmail.text.clear()

                // Send thank-you email via EmailSender.kt
                EmailSender.sendEmail(
                    toEmail = email,
                    subject = "Thank you for subscribing to Lungisa NPO",
                    body = """
                        Dear Subscriber,
                        
                        Thank you for subscribing to updates from Lungisa NPO. 
                        We are excited to keep you informed about our latest news, events, and opportunities.
                        
                        Best Regards,
                        Lungisa NPO Team
                    """.trimIndent()
                )
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to subscribe: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
