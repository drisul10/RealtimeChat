package soel.dri.kotlin_socketio_chat

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.view.LayoutInflater

class ChatAdapter(private val messageList: List<Message>): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(messageList[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickname: TextView = view.findViewById(R.id.nickname)
        val message: TextView = view.findViewById(R.id.message)

        fun bindItem(list: Message) {
            nickname.text = list.nickname
            message.text = list.message
        }
    }
}