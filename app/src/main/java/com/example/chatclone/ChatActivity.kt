package com.example.chatclone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessageAdapter
    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var tvContactName: TextView
    private lateinit var typingIndicator: TextView
    private lateinit var btnAttachment: ImageButton
    private lateinit var btnVoice: ImageButton
    private lateinit var voiceRecordingView: LinearLayout
    private lateinit var btnRecord: Button
    private lateinit var tvRecordingTime: TextView

    private val messages = mutableListOf<Message>()
    private val handler = Handler(Looper.getMainLooper())
    private var isTyping = false
    private var typingRunnable: Runnable? = null
    private var isRecording = false
    private var recordingTimer: Timer? = null
    private var recordingSeconds = 0
    private var currentPhotoPath: String? = null
    private var selectedImageUri: Uri? = null

    // Launchers for gallery, camera, permissions
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            showMediaPreview("image", it)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && currentPhotoPath != null) {
            val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", File(currentPhotoPath!!))
            selectedImageUri = uri
            showMediaPreview("image", uri)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions.values.all { it }) {
            // permissions granted, action will be triggered again by user
        } else {
            Toast.makeText(this, "Permissions required for media", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        tvContactName = findViewById(R.id.tvContactName)
        val chatName = intent.getStringExtra("chat_name") ?: "User"
        tvContactName.text = chatName

        recyclerView = findViewById(R.id.recyclerViewChat)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)
        typingIndicator = findViewById(R.id.typingIndicator)
        btnAttachment = findViewById(R.id.btnAttachment)
        btnVoice = findViewById(R.id.btnVoice)
        voiceRecordingView = findViewById(R.id.voiceRecordingView)
        btnRecord = findViewById(R.id.btnRecord)
        tvRecordingTime = findViewById(R.id.tvRecordingTime)

        adapter = MessageAdapter(messages)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        loadSampleMessages()

        btnSend.setOnClickListener {
            val text = etMessage.text.toString().trim()
            if (text.isNotEmpty()) {
                sendMessage(text)
                etMessage.text.clear()
                simulateReceiverTyping()
            }
        }

        btnAttachment.setOnClickListener { showAttachmentOptions() }

        btnVoice.setOnClickListener {
            if (voiceRecordingView.visibility == View.VISIBLE) {
                voiceRecordingView.visibility = View.GONE
            } else {
                voiceRecordingView.visibility = View.VISIBLE
                scrollToBottom()
            }
        }

        btnRecord.setOnClickListener {
            if (isRecording) stopRecording() else startRecording()
        }

        // Typing detection
        etMessage.setOnKeyListener { _, _, _ ->
            if (!isTyping) {
                showTypingIndicator(true)
                isTyping = true
                typingRunnable?.let { handler.removeCallbacks(it) }
                typingRunnable = Runnable {
                    isTyping = false
                    showTypingIndicator(false)
                }
                handler.postDelayed(typingRunnable!!, 1500)
            }
            false
        }
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false) // hide default title, keep custom TextView

    }

    private fun loadSampleMessages() {
        val timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        messages.add(Message("Hey! How are you?", false, timestamp, isSeen = true))
        messages.add(Message("I'm good, thanks! You?", true, timestamp, isSeen = true, isDelivered = true))
        messages.add(Message("Doing great! Let's chat.", false, timestamp, isSeen = true))
        adapter.notifyDataSetChanged()
        scrollToBottom()
    }

    private fun sendMessage(text: String) {
        val timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val message = Message(text, true, timestamp, isSeen = false, isDelivered = false)
        adapter.addMessage(message)
        scrollToBottom()
        // Simulate delivery after short delay
        handler.postDelayed({
            val pos = messages.size - 1
            if (pos >= 0 && messages[pos].isSent) {
                adapter.updateMessageStatus(pos, false, true)
            }
        }, 500)
    }

    private fun sendMediaMessage(type: String, uri: Uri?) {
        val timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val message = Message(
            text = if (type == "image") "📷 Image" else "🎤 Voice",
            isSent = true,
            timestamp = timestamp,
            mediaType = if (type == "image") 1 else 2,
            mediaUrl = uri?.toString(),
            duration = if (type == "voice") "${recordingSeconds / 60}:${String.format("%02d", recordingSeconds % 60)}" else null
        )
        adapter.addMessage(message)
        scrollToBottom()
        // Simulate delivery & seen after delay
        handler.postDelayed({
            val pos = messages.size - 1
            if (pos >= 0 && messages[pos].isSent) {
                adapter.updateMessageStatus(pos, false, true)
            }
        }, 500)
        // Auto reply (optional)
        simulateReceiverReply()
    }

    private fun simulateReceiverTyping() {
        showTypingIndicator(true)
        handler.postDelayed({
            showTypingIndicator(false)
            val reply = getRandomReply()
            val timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            val receivedMsg = Message(reply, false, timestamp, isSeen = true)
            adapter.addMessage(receivedMsg)
            scrollToBottom()
            markLastSentMessageAsSeen()
        }, 2000)
    }

    private fun simulateReceiverReply() {
        handler.postDelayed({
            val reply = getRandomReply()
            val timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            val receivedMsg = Message(reply, false, timestamp, isSeen = true)
            adapter.addMessage(receivedMsg)
            scrollToBottom()
        }, 2000)
    }

    private fun markLastSentMessageAsSeen() {
        for (i in messages.indices.reversed()) {
            if (messages[i].isSent) {
                adapter.updateMessageStatus(i, true, true)
                break
            }
        }
    }

    private fun showTypingIndicator(show: Boolean) {
        typingIndicator.visibility = if (show) View.VISIBLE else View.GONE
        if (show) scrollToBottom()
    }

    private fun scrollToBottom() {
        recyclerView.post {
            if (adapter.itemCount > 0) {
                recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun getRandomReply(): String {
        val replies = listOf(
            "That's interesting! 😊", "Tell me more. 👍", "Haha, nice! 😂",
            "I see... 👀", "Okay, let's meet soon. 🤝", "Cool! 👌", "😂😂😂", "😍😍"
        )
        return replies.random()
    }

    // Media handling
    private fun showAttachmentOptions() {
        val options = arrayOf("Gallery", "Camera")
        AlertDialog.Builder(this).setTitle("Send Media")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkPermissionsAndOpenGallery()
                    1 -> checkPermissionsAndOpenCamera()
                }
            }.show()
    }

    private fun checkPermissionsAndOpenGallery() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            galleryLauncher.launch("image/*")
        } else {
            requestPermissionLauncher.launch(permissions)
        }
    }

    private fun checkPermissionsAndOpenCamera() {
        val perms = mutableListOf<String>().apply {
            if (ContextCompat.checkSelfPermission(this@ChatActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this@ChatActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (perms.isEmpty()) {
            dispatchTakePictureIntent()
        } else {
            requestPermissionLauncher.launch(perms.toTypedArray())
        }
    }

    private fun dispatchTakePictureIntent() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFile = File(getExternalFilesDir(null), "IMG_$timeStamp.jpg")
        currentPhotoPath = imageFile.absolutePath
        val uri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", imageFile)
        cameraLauncher.launch(uri)
    }

    private fun showMediaPreview(type: String, uri: Uri) {
        val previewView = layoutInflater.inflate(R.layout.dialog_media_preview, null)
        val imageView = previewView.findViewById<ImageView>(R.id.previewImage)
        val btnSend = previewView.findViewById<Button>(R.id.btnSendPreview)
        val btnCancel = previewView.findViewById<Button>(R.id.btnCancelPreview)

        if (type == "image") {
            Glide.with(this).load(uri).into(imageView)
        }

        val dialog = AlertDialog.Builder(this).setView(previewView).setCancelable(true).create()
        btnSend.setOnClickListener {
            sendMediaMessage(type, uri)
            dialog.dismiss()
        }
        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    // Voice recording simulation
    private fun startRecording() {
        isRecording = true
        btnRecord.text = "⏹ Stop"
        recordingSeconds = 0
        tvRecordingTime.text = "0:00"
        recordingTimer = Timer()
        recordingTimer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                recordingSeconds++
                runOnUiThread {
                    tvRecordingTime.text = "${recordingSeconds / 60}:${String.format("%02d", recordingSeconds % 60)}"
                }
            }
        }, 0, 1000)
        Toast.makeText(this, "Recording started (simulated)", Toast.LENGTH_SHORT).show()
    }

    private fun stopRecording() {
        isRecording = false
        btnRecord.text = "🎤 Hold to Record"
        recordingTimer?.cancel()
        Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
        voiceRecordingView.visibility = View.GONE
        sendMediaMessage("voice", null)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}