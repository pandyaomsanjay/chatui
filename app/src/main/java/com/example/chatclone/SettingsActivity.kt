package com.example.chatclone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SettingsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SettingsAdapter


    private val settingsOptions = listOf(
        "Profile" to R.drawable.ic_profile,
        "Notifications" to R.drawable.ic_settings,
        "Privacy" to R.drawable.ic_settings,
        "Chats" to R.drawable.ic_settings,
        "Logout" to R.drawable.ic_logout
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.title = "Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerViewSettings)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = SettingsAdapter(settingsOptions) { option ->
            when (option) {
                "Profile" -> startActivity(Intent(this, UserProfileActivity::class.java))
                "Notifications" -> startActivity(Intent(this, NotificationSettingsActivity::class.java))
                "Privacy" -> startActivity(Intent(this, PrivacySettingsActivity::class.java))
                "Chats" -> Toast.makeText(this, "Chat Settings coming soon", Toast.LENGTH_SHORT).show()
                "Logout" -> showLogoutDialog()
            }
        }
        recyclerView.adapter = adapter
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                performLogout()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun performLogout() {
        // Clear user session
        getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().clear().apply()
        // Clear any other app data if needed
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}