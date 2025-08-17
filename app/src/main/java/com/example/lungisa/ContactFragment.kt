package com.example.lungisa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class ContactFragment : Fragment() {

    private lateinit var inputFirstName: EditText
    private lateinit var inputLastName: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputSubject: EditText
    private lateinit var inputMessage: EditText
    private lateinit var sendButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        inputFirstName = view.findViewById(R.id.input_first_name)
        inputLastName = view.findViewById(R.id.input_last_name)
        inputEmail = view.findViewById(R.id.input_email)
        inputSubject = view.findViewById(R.id.input_subject)
        inputMessage = view.findViewById(R.id.input_message)
        sendButton = view.findViewById(R.id.send_message_button)

        sendButton.setOnClickListener {
            sendMessageToFirebase()
        }

        return view
    }

    private fun sendMessageToFirebase() {
        val firstName = inputFirstName.text.toString().trim()
        val lastName = inputLastName.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val subject = inputSubject.text.toString().trim()
        val message = inputMessage.text.toString().trim()

        // Validate fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
            subject.isEmpty() || message.isEmpty()
        ) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Current date and time
        val currentDateTime = SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())

        val contact = Contact(firstName, lastName, email, subject, message, currentDateTime)

        val database = FirebaseDatabase.getInstance()
        val contactsRef = database.getReference("Contacts")

        // Push contact to Firebase
        contactsRef.push().setValue(contact)
            .addOnSuccessListener {
                Toast.makeText(context, "Message sent successfully!", Toast.LENGTH_SHORT).show()

                // Clear form fields
                inputFirstName.text.clear()
                inputLastName.text.clear()
                inputEmail.text.clear()
                inputSubject.text.clear()
                inputMessage.text.clear()

                // Send thank-you email automatically using EmailSender.kt
                val emailSubject = "Thank you for contacting Lungisa NPO"
                val emailBody = """
                    Dear $firstName,
                    
                    Thank you for reaching out to Lungisa NPO. We have received your message and will get back to you shortly.
                    
                    Best Regards,
                    Lungisa NPO Team
                """.trimIndent()

                EmailSender.sendEmail(email, emailSubject, emailBody)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to send message: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
