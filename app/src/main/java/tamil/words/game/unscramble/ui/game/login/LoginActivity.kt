package tamil.words.game.unscramble.ui.game.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import tamil.words.game.unscramble.R
import tamil.words.game.unscramble.ui.game.LevelsActivity
import tamil.words.game.unscramble.ui.game.datas.entries

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val usertext: EditText = findViewById(R.id.edit_text)
        val button: Button = findViewById(R.id.enter)
        button.setOnClickListener {
            entries.username = usertext.text.toString().trim()
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