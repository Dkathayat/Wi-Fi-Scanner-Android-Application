package com.kathayat.railscarmaapplication.utils

import android.app.ProgressDialog
import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogBuilder(private val context: Context) {
    fun showDialog(title: String, message: String) {
        // Create the MaterialAlertDialogBuilder instance
        val dialogBuilder = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                // Dismiss the dialog when OK is pressed
                dialog.dismiss()
            }

        // Show the dialog
        dialogBuilder.show()
    }
}