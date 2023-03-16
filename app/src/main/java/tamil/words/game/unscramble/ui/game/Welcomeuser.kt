package tamil.words.game.unscramble.ui.game

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

import tamil.words.game.unscramble.ui.game.login.LoginActivity
import com.google.android.material.textview.MaterialTextView
import tamil.words.game.unscramble.R

class Welcomeuser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcomeuser)
        val sharedPref = this?.getSharedPreferences("sharing",Context.MODE_PRIVATE) ?: return
        val username = sharedPref.getString("username", "")
        val userscore = sharedPref.getInt(username,0)
        Log.d("welcomeuser",userscore.toString())
        val text_username : MaterialTextView = findViewById(R.id.text_username)
        val value : String  = username ?:""
        text_username.setText(value)
        val textcreate : MaterialTextView = findViewById(R.id.Create)
        textcreate.paint?.isUnderlineText=true
        textcreate.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val button : Button = findViewById(R.id.enter)
        button.setOnClickListener{
            val intent = Intent(this, LevelsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed()
    {
        finishAffinity()
    }


}