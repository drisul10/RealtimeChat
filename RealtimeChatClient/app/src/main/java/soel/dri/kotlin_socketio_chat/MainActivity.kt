package soel.dri.kotlin_socketio_chat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.content.Intent
import android.view.View


class MainActivity : AppCompatActivity() {

    private lateinit var inputNickname: EditText
    private lateinit var btnStartChat: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputNickname = findViewById<View>(R.id.nickname) as EditText
        btnStartChat = findViewById<View>(R.id.startchat) as Button

        btnStartChat.setOnClickListener {
            if (!inputNickname.text.toString().isEmpty()) {
                val i = Intent(this@MainActivity, ChatActivity::class.java)
                i.putExtra(NICK, inputNickname.text.toString())
                startActivity(i)
            }
        }
    }

    companion object {
        const val NICK = "NICKNAME"
    }
}
