package com.example.chatclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(
    private var chats: List<Chat>,
    private val onItemClick: (Chat) -> Unit
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chats[position]
        holder.nameText.text = chat.name
        holder.messageText.text = chat.lastMessage
        holder.timeText.text = chat.lastMessageTime
        holder.avatar.setImageResource(chat.avatarRes)

        // Online status
        holder.onlineIndicator.visibility = if (chat.isOnline) View.VISIBLE else View.GONE

        // Unread count
        if (chat.unreadCount > 0) {
            holder.unreadBadge.visibility = View.VISIBLE
            holder.unreadBadge.text = if (chat.unreadCount > 99) "99+" else chat.unreadCount.toString()
        } else {
            holder.unreadBadge.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { onItemClick(chat) }
    }

    override fun getItemCount() = chats.size

    fun updateList(newList: List<Chat>) {
        chats = newList
        notifyDataSetChanged()
    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        val onlineIndicator: View = itemView.findViewById(R.id.viewOnline)
        val nameText: TextView = itemView.findViewById(R.id.tvChatName)
        val messageText: TextView = itemView.findViewById(R.id.tvLastMessage)
        val timeText: TextView = itemView.findViewById(R.id.tvTimestamp)
        val unreadBadge: TextView = itemView.findViewById(R.id.badgeUnread)
    }
}