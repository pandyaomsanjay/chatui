package com.example.chatclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MessageAdapter(
    private val messages: MutableList<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SENT_TEXT = 1
        private const val TYPE_RECEIVED_TEXT = 2
        private const val TYPE_SENT_IMAGE = 3
        private const val TYPE_RECEIVED_IMAGE = 4
        private const val TYPE_SENT_VOICE = 5
        private const val TYPE_RECEIVED_VOICE = 6
    }

    override fun getItemViewType(position: Int): Int {
        val msg = messages[position]
        return when {
            msg.isSent && msg.mediaType == 1 -> TYPE_SENT_IMAGE
            !msg.isSent && msg.mediaType == 1 -> TYPE_RECEIVED_IMAGE
            msg.isSent && msg.mediaType == 2 -> TYPE_SENT_VOICE
            !msg.isSent && msg.mediaType == 2 -> TYPE_RECEIVED_VOICE
            msg.isSent -> TYPE_SENT_TEXT
            else -> TYPE_RECEIVED_TEXT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_SENT_TEXT -> {
                val view = inflater.inflate(R.layout.item_message_sent, parent, false)
                SentTextHolder(view)
            }
            TYPE_RECEIVED_TEXT -> {
                val view = inflater.inflate(R.layout.item_message_received, parent, false)
                ReceivedTextHolder(view)
            }
            TYPE_SENT_IMAGE -> {
                val view = inflater.inflate(R.layout.item_message_image_sent, parent, false)
                SentImageHolder(view)
            }
            TYPE_RECEIVED_IMAGE -> {
                val view = inflater.inflate(R.layout.item_message_image_received, parent, false)
                ReceivedImageHolder(view)
            }
            TYPE_SENT_VOICE -> {
                val view = inflater.inflate(R.layout.item_message_voice_sent, parent, false)
                SentVoiceHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_message_voice_received, parent, false)
                ReceivedVoiceHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messages[position]
        when (holder) {
            is SentTextHolder -> {
                holder.messageText.text = msg.text
                holder.timestampText.text = msg.timestamp
                setStatusText(holder.statusText, msg)
            }
            is ReceivedTextHolder -> {
                holder.messageText.text = msg.text
                holder.timestampText.text = msg.timestamp
            }
            is SentImageHolder -> {
                Glide.with(holder.itemView.context).load(msg.mediaUrl).into(holder.imageView)
                holder.timestampText.text = msg.timestamp
                setStatusText(holder.statusText, msg)
            }
            is ReceivedImageHolder -> {
                Glide.with(holder.itemView.context).load(msg.mediaUrl).into(holder.imageView)
                holder.timestampText.text = msg.timestamp
            }
            is SentVoiceHolder -> {
                holder.durationText.text = msg.duration ?: "0:00"
                holder.timestampText.text = msg.timestamp
                setStatusText(holder.statusText, msg)
                holder.playButton.setOnClickListener {
                    // In real app, implement MediaPlayer
                    android.widget.Toast.makeText(holder.itemView.context, "Play voice (simulated)", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            is ReceivedVoiceHolder -> {
                holder.durationText.text = msg.duration ?: "0:00"
                holder.timestampText.text = msg.timestamp
                holder.playButton.setOnClickListener {
                    android.widget.Toast.makeText(holder.itemView.context, "Play voice (simulated)", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setStatusText(statusView: TextView, msg: Message) {
        val status = when {
            msg.isSeen -> "✓✓" // blue double tick (we'll color it)
            msg.isDelivered -> "✓✓"
            else -> "✓"
        }
        statusView.text = status
        if (msg.isSeen) {
            statusView.setTextColor(statusView.context.getColor(android.R.color.holo_blue_light))
        } else {
            statusView.setTextColor(statusView.context.getColor(android.R.color.darker_gray))
        }
    }

    override fun getItemCount() = messages.size

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun updateMessageStatus(position: Int, isSeen: Boolean, isDelivered: Boolean) {
        if (position in messages.indices && messages[position].isSent) {
            messages[position] = messages[position].copy(isSeen = isSeen, isDelivered = isDelivered)
            notifyItemChanged(position)
        }
    }

    // View Holders
    class SentTextHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.tvMessageSent)
        val timestampText: TextView = itemView.findViewById(R.id.tvTimestampSent)
        val statusText: TextView = itemView.findViewById(R.id.tvStatus)
    }
    class ReceivedTextHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.tvMessageReceived)
        val timestampText: TextView = itemView.findViewById(R.id.tvTimestampReceived)
    }
    class SentImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivImageSent)
        val timestampText: TextView = itemView.findViewById(R.id.tvTimestampSent)
        val statusText: TextView = itemView.findViewById(R.id.tvStatus)
    }
    class ReceivedImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivImageReceived)
        val timestampText: TextView = itemView.findViewById(R.id.tvTimestampReceived)
    }
    class SentVoiceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playButton: ImageButton = itemView.findViewById(R.id.btnPlayVoice)
        val durationText: TextView = itemView.findViewById(R.id.tvVoiceDuration)
        val timestampText: TextView = itemView.findViewById(R.id.tvTimestampSent)
        val statusText: TextView = itemView.findViewById(R.id.tvStatus)
    }
    class ReceivedVoiceHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playButton: ImageButton = itemView.findViewById(R.id.btnPlayVoice)
        val durationText: TextView = itemView.findViewById(R.id.tvVoiceDuration)
        val timestampText: TextView = itemView.findViewById(R.id.tvTimestampReceived)
    }
}