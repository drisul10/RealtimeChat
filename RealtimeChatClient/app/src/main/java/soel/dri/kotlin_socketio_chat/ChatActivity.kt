package soel.dri.kotlin_socketio_chat

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import java.net.URISyntaxException
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject

class ChatActivity: AppCompatActivity() {

    private lateinit var nickname: String
    private lateinit var socket: Socket
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageList: List<Message>
    private lateinit var btnSend: Button
    private lateinit var btnLeft: Button
    private lateinit var textMessage: EditText
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        btnSend = findViewById<View>(R.id.send) as Button
        btnLeft = findViewById<View>(R.id.left) as Button
        textMessage = findViewById<View>(R.id.textMessage) as EditText

        val intent = intent
        nickname = intent.getStringExtra(NICK)

        try {
            socket = IO.socket("http://192.168.1.11:3000")
            socket.connect()

            socket.emit("join", nickname)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        //setting up recycler
        messageList = ArrayList()
        recyclerView = findViewById<View>(R.id.messageList) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        recyclerView.itemAnimator = DefaultItemAnimator()

        btnSend.setOnClickListener {
            if (!textMessage.text.toString().isEmpty()) {
                socket.emit("messagedetection", nickname, textMessage.text.toString())

//                textMessage.setText(" ")
            }
        }

        btnLeft.setOnClickListener {
            socket.emit("left", nickname)
            socket.disconnect()
            val i = Intent(this@ChatActivity, MainActivity::class.java)
            startActivity(i)
        }

        socket.on("userjoinedthechat") { args ->
            runOnUiThread {
                val data = args[0] as String

                Toast.makeText(this@ChatActivity, data, Toast.LENGTH_SHORT).show()
            }
        }

        socket.on("message") { args ->
            runOnUiThread {
                val data = args[0] as JSONObject
                try {
                    //extract data from fired event
                    val nickname = data.getString("senderNickname")
                    val message = data.getString("message")

                    // make instance of message
                    val m = Message(nickname, message)

                    //add the message to the messageList
                    (messageList as ArrayList<Message>).add(m)

                    // add the new updated list to the dapter
                    adapter = ChatAdapter(messageList)

                    // notify the adapter to update the recycler view
                    adapter.notifyDataSetChanged()

                    //set the adapter for the recycler view
                    recyclerView.adapter = adapter

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }

    companion object {
        const val NICK = "NICKNAME"
    }
}