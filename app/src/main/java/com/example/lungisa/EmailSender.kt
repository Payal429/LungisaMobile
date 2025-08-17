package com.example.lungisa

import android.os.StrictMode
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

object EmailSender {

    fun sendEmail(toEmail: String, subject: String, body: String) {
        // Allow network operation on main thread (for simplicity)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val fromEmail = "payal.sunil429@gmail.com"        // Replace with your Gmail
        val password = ""     // Use Gmail App Password

        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(fromEmail, password)
            }
        })

        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(fromEmail))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
            message.subject = subject
            message.setText(body)

            Transport.send(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
