package com.example.chatclone

import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class NotificationSettingsActivity : AppCompatActivity() {

    private lateinit var switchMessageNotifications: Switch
    private lateinit var switchGroupNotifications: Switch
    private lateinit var switchSound: Switch
    private lateinit var switchVibrate: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notifications"

        switchMessageNotifications = findViewById(R.id.switchMessageNotifications)
        switchGroupNotifications = findViewById(R.id.switchGroupNotifications)
        switchSound = findViewById(R.id.switchSound)
        switchVibrate = findViewById(R.id.switchVibrate)

        loadSettings()

        switchMessageNotifications.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("message_notifications", isChecked)
            Toast.makeText(this, "Message notifications ${if (isChecked) "on" else "off"}", Toast.LENGTH_SHORT).show()
        }
        switchGroupNotifications.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("group_notifications", isChecked)
        }
        switchSound.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("notification_sound", isChecked)
        }
        switchVibrate.setOnCheckedChangeListener { _, isChecked ->
            saveSetting("notification_vibrate", isChecked)
        }
    }

    private fun loadSettings() {
        val prefs = getSharedPreferences("SettingsPrefs", MODE_PRIVATE)
        switchMessageNotifications.isChecked = prefs.getBoolean("message_notifications", true)
        switchGroupNotifications.isChecked = prefs.getBoolean("group_notifications", true)
        switchSound.isChecked = prefs.getBoolean("notification_sound", true)
        switchVibrate.isChecked = prefs.getBoolean("notification_vibrate", true)
    }

    private fun saveSetting(key: String, value: Boolean) {
        getSharedPreferences("SettingsPrefs", MODE_PRIVATE).edit().putBoolean(key, value).apply()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}