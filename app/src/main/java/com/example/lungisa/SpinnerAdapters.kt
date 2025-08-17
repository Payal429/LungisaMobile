package com.example.lungisa

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner

object SpinnerAdapters {

    fun setupAvailabilitySpinner(context: Context, spinner: Spinner) {
        val availabilityOptions = listOf("Weekdays", "Weekends", "Evenings")
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, availabilityOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun setupRoleSpinner(context: Context, spinner: Spinner) {
        val roleOptions = listOf("Healthcare", "Education", "Community Outreach", "Administration")
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, roleOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
