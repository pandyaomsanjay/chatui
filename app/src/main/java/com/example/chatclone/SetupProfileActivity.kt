package com.example.chatclone

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SetupProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etBio: EditText
    private lateinit var ivAvatar: ImageView
    private lateinit var btnSave: Button
    private var selectedImageUri: Uri? = null
    private val storageRef = FirebaseStorage.getInstance().reference
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile)

        etName = findViewById(R.id.etName)
        etBio = findViewById(R.id.etBio)
        ivAvatar = findViewById(R.id.ivAvatar)
        btnSave = findViewById(R.id.btnSave)

        ivAvatar.setOnClickListener { openImagePicker() }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            saveUserToFirestore(name, etBio.text.toString().trim())
        }
    }

    private fun saveUserToFirestore(name: String, bio: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            return
        }

        val uid = currentUser.uid
        val phone = currentUser.phoneNumber ?: ""

        val userMap = mutableMapOf<String, Any>(
            "uid" to uid,
            "name" to name,
            "bio" to bio,
            "phone" to phone,
            "photoUrl" to "",
            "online" to true,
            "lastSeen" to FieldValue.serverTimestamp()
        )

        if (selectedImageUri != null) {
            val fileRef = storageRef.child("profile_images/$uid.jpg")
            fileRef.putFile(selectedImageUri!!).addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    userMap["photoUrl"] = uri.toString()
                    finalizeSave(userMap)
                }
            }.addOnFailureListener {
                finalizeSave(userMap)
            }
        } else {
            finalizeSave(userMap)
        }
    }

    private fun finalizeSave(map: Map<String, Any>) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        db.collection("users").document(uid).set(map)
            .addOnSuccessListener {
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            selectedImageUri = result.data?.data
            Glide.with(this).load(selectedImageUri).circleCrop().into(ivAvatar)
        }
    }
}
