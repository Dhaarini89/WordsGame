package com.example.android.unscramble.ui.game.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.android.unscramble.R
import com.example.android.unscramble.ui.game.LevelsActivity
import com.example.android.unscramble.ui.game.datas.entries
import com.google.android.material.textview.MaterialTextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val usertext: EditText = findViewById(R.id.edit_text)
        val button: Button = findViewById(R.id.enter)
        button.setOnClickListener {
            entries.username = usertext.text.toString()
            val intent = Intent(this, LevelsActivity::class.java)
            val sharedPref = this.getSharedPreferences("sharing", Context.MODE_PRIVATE)
            with(sharedPref.edit())
            {
                putString("username", entries.username)
                putInt(entries.username, entries.scorecompanion)
                apply()
                commit()
            }
            startActivity(intent)
        }

    }


}