package com.example.chatclone

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class PrivacySettingsActivity : AppCompatActivity() {

    private lateinit var spinnerLastSeen: Spinner
    private lateinit var spinnerProfilePhoto: Spinner
    private lateinit var spinnerStatus: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Privacy"

        spinnerLastSeen = findViewById(R.id.spinnerLastSeen)
        spinnerProfilePhoto = findViewById(R.id.spinnerProfilePhoto)
        spinnerStatus = findViewById(R.id.spinnerStatus)

        val options = arrayOf("Everyone", "My Contacts", "Nobody")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerLastSeen.adapter = adapter
        spinnerProfilePhoto.adapter = adapter
        spinnerStatus.adapter = adapter

        loadPrivacySettings()

        spinnerLastSeen.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                saveSetting("last_seen_privacy", position)
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
        spinnerProfilePhoto.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                saveSetting("profile_photo_privacy", position)
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
        spinnerStatus.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                saveSetting("status_privacy", position)
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

    private fun loadPrivacySettings() {
        val prefs = getSharedPreferences("SettingsPrefs", MODE_PRIVATE)
        spinnerLastSeen.setSelection(prefs.getInt("last_seen_privacy", 0))
        spinnerProfilePhoto.setSelection(prefs.getInt("profile_photo_privacy", 0))
        spinnerStatus.setSelection(prefs.getInt("status_privacy", 0))
    }

    private fun saveSetting(key: String, value: Int) {
        getSharedPreferences("SettingsPrefs", MODE_PRIVATE).edit().putInt(key, value).apply()
        Toast.makeText(this, "Privacy updated", Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}