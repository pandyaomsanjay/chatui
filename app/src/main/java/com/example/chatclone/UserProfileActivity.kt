package com.example.chatclone

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class UserProfileActivity : AppCompatActivity() {

    private lateinit var ivAvatar: ImageView
    private lateinit var tvChangePhoto: TextView
    private lateinit var etDisplayName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etBio: EditText
    private lateinit var btnSave: Button

    private var currentPhotoUri: Uri? = null
    private var currentPhotoPath: String? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            currentPhotoUri = it
            ivAvatar.setImageURI(it)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            currentPhotoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", File(currentPhotoPath!!))
            ivAvatar.setImageURI(currentPhotoUri)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.values.all { it }) {
            showImagePickerOptions()
        } else {
            Toast.makeText(this, "Permissions required to change photo", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        supportActionBar?.title = "Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ivAvatar = findViewById(R.id.ivProfileAvatar)
        tvChangePhoto = findViewById(R.id.tvChangePhoto)
        etDisplayName = findViewById(R.id.etDisplayName)
        etPhone = findViewById(R.id.etPhoneNumber)
        etBio = findViewById(R.id.etBio)
        btnSave = findViewById(R.id.btnSaveProfile)

        loadUserData()

        tvChangePhoto.setOnClickListener {
            checkPermissionsAndPickImage()
        }

        btnSave.setOnClickListener {
            saveUserData()
        }
    }

    private fun loadUserData() {
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val name = sharedPref.getString("display_name", "John Doe")
        val phone = sharedPref.getString("phone_number", "+1234567890")
        val bio = sharedPref.getString("bio", "Available for chat")
        val photoUriString = sharedPref.getString("profile_photo", null)

        etDisplayName.setText(name)
        etPhone.setText(phone)
        etBio.setText(bio)

        if (!photoUriString.isNullOrEmpty()) {
            try {
                currentPhotoUri = Uri.parse(photoUriString)
                Glide.with(this).load(currentPhotoUri).circleCrop().into(ivAvatar)
            } catch (e: Exception) {
                ivAvatar.setImageResource(R.drawable.avatar_placeholder)
            }
        } else {
            ivAvatar.setImageResource(R.drawable.avatar_placeholder)
        }
    }

    private fun saveUserData() {
        val newName = etDisplayName.text.toString().trim()
        val newBio = etBio.text.toString().trim()

        if (newName.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPref.edit().apply {
            putString("display_name", newName)
            putString("bio", newBio)
            currentPhotoUri?.let { putString("profile_photo", it.toString()) }
            apply()
        }
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun checkPermissionsAndPickImage() {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.CAMERA)

        if (permissions.isNotEmpty()) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        } else {
            showImagePickerOptions()
        }
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        AlertDialog.Builder(this).setTitle("Profile Picture")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> dispatchTakePictureIntent()
                    1 -> galleryLauncher.launch("image/*")
                }
            }.show()
    }

    private fun dispatchTakePictureIntent() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFile = File(getExternalFilesDir(null), "Profile_$timeStamp.jpg")
        currentPhotoPath = imageFile.absolutePath
        val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", imageFile)
        cameraLauncher.launch(uri)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}