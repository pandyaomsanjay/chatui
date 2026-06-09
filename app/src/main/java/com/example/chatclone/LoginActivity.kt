package com.example.chatclone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hbb20.CountryCodePicker

class LoginActivity : AppCompatActivity() {

    private lateinit var ccp: CountryCodePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ccp = findViewById(R.id.ccp)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // Link the CountryCodePicker to the phone input field
        ccp.registerCarrierNumberEditText(etPhone)
        ccp.setDefaultCountryUsingNameCode("IN")

        btnLogin.setOnClickListener {
            if (!ccp.isValidFullNumber) {
                Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val phoneNumber = ccp.fullNumberWithPlus

            // Save phone number for later use (e.g., in SharedPreferences)
            getSharedPreferences("UserPrefs", MODE_PRIVATE).edit()
                .putString("phone_number", phoneNumber)
                .apply()

            // For demo: always go to HomeActivity (or SetupProfileActivity if you want first-time setup)
            // Here we directly go to HomeActivity. If you need profile setup on first launch,
            // you can check a flag and go to SetupProfileActivity instead.
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}