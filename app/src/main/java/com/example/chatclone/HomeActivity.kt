package com.example.chatclone

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var fabNewChat: FloatingActionButton
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var toolbar: MaterialToolbar
    private val chatList = mutableListOf<Chat>()
    private var fullChatList = listOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Chats"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        recyclerView = findViewById(R.id.recyclerViewChats)
        fabNewChat = findViewById(R.id.fabNewChat)
        bottomNav = findViewById(R.id.bottomNav)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadSampleChats()
        adapter = ChatAdapter(chatList) { chat ->
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("chat_name", chat.name)
            intent.putExtra("chat_id", chat.id)
            // Mark as read when opened
            chat.unreadCount = 0
            adapter.updateList(chatList)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        fabNewChat.setOnClickListener {
            Toast.makeText(this, "New Chat - Coming Soon", Toast.LENGTH_SHORT).show()
        }

        // Bottom navigation listener
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_chats -> {
                    // Already on chats – could refresh or do nothing
                    true
                }
                R.id.nav_status -> {
                    Toast.makeText(this, "Status – Coming soon", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_calls -> {
                    Toast.makeText(this, "Calls – Coming soon", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadSampleChats() {
        fullChatList = listOf(
            Chat("1", "Alice Johnson", "Hey, how are you?", "10:30 AM", isOnline = true, unreadCount = 2),
            Chat("2", "Bob Smith", "Let's meet tomorrow", "09:15 AM", isOnline = false, unreadCount = 0),
            Chat("3", "Charlie Brown", "Great work!", "Yesterday", isOnline = true, unreadCount = 5),
            Chat("4", "Diana Prince", "See you at the party", "Yesterday", isOnline = false, unreadCount = 0),
            Chat("5", "Eve Adams", "I sent the files", "Monday", isOnline = false, unreadCount = 1)
        )
        chatList.clear()
        chatList.addAll(fullChatList)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Search chats..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterChats(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterChats(newText)
                return true
            }
        })
        return true
    }

    private fun filterChats(query: String?) {
        if (query.isNullOrEmpty()) {
            chatList.clear()
            chatList.addAll(fullChatList)
        } else {
            val filtered = fullChatList.filter { chat ->
                chat.name.contains(query, ignoreCase = true) ||
                        chat.lastMessage.contains(query, ignoreCase = true)
            }
            chatList.clear()
            chatList.addAll(filtered)
        }
        adapter.updateList(chatList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_profile -> {
                startActivity(Intent(this, UserProfileActivity::class.java))
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
