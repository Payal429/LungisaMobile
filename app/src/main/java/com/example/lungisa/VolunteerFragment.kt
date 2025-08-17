package com.example.lungisa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class VolunteerFragment : Fragment() {

    private lateinit var inputFullName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPhone: EditText
    private lateinit var spinnerAvailability: Spinner
    private lateinit var spinnerRole: Spinner
    private lateinit var submitButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_volunteer, container, false)

        // Initialize views
        inputFullName = view.findViewById(R.id.input_full_name)
        inputEmail = view.findViewById(R.id.input_email)
        inputPhone = view.findViewById(R.id.input_phone)
        spinnerAvailability = view.findViewById(R.id.spinnerAvailability)
        spinnerRole = view.findViewById(R.id.spinnerRole)
        submitButton = view.findViewById(R.id.submit_volunteer_button)

        submitButton.setOnClickListener {
            submitVolunteer()
        }
        // Initialize spinners
        spinnerRole = view.findViewById(R.id.spinnerRole)

        // Set adapters using helper file
        SpinnerAdapters.setupAvailabilitySpinner(requireContext(), spinnerAvailability)
        SpinnerAdapters.setupRoleSpinner(requireContext(), spinnerRole)

        return view
    }

    private fun submitVolunteer() {
        val fullName = inputFullName.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val phone = inputPhone.text.toString().trim()
        val availability = spinnerAvailability.selectedItem.toString()
        val rolePreference = spinnerRole.selectedItem.toString()

        // Validate input
        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || availability.isEmpty() || rolePreference.isEmpty()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Current date and time
        val currentDateTime = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        // Create volunteer object
        val volunteer = Volunteer(fullName, email, phone, availability, rolePreference, currentDateTime)

        // Push to Firebase Realtime Database
        val database = FirebaseDatabase.getInstance().getReference("Volunteers")
        database.push().setValue(volunteer)
            .addOnSuccessListener {
                Toast.makeText(context, "Volunteer submitted successfully!", Toast.LENGTH_SHORT).show()
                clearFields()
                sendThankYouEmail(email, fullName) // Send only one email
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to submit volunteer: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sendThankYouEmail(email: String, fullName: String) {
        val subject = "Thank you for volunteering with Lungisa NPO"
        val body = """
            Dear $fullName,
            
            Thank you for signing up as a volunteer with Lungisa NPO. We appreciate your interest and will get back to you soon with next steps.
            
            Best Regards,
            Lungisa NPO Team
        """.trimIndent()

        EmailSender.sendEmail(email, subject, body)
    }

    private fun clearFields() {
        inputFullName.text.clear()
        inputEmail.text.clear()
        inputPhone.text.clear()
        spinnerAvailability.setSelection(0)
        spinnerRole.setSelection(0)
    }
}
