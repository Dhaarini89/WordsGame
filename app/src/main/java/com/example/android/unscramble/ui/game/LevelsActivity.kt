package com.example.android.unscramble.ui.game


import android.app.Activity
import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.android.unscramble.MainActivity
import com.example.android.unscramble.R
import com.example.android.unscramble.ui.game.adapter.CustomAdapter

import com.example.android.unscramble.ui.game.datas.LevelsViewModel
import com.example.android.unscramble.ui.game.datas.entries
import com.example.android.unscramble.ui.game.datas.entries.Companion.counter
import com.example.android.unscramble.ui.game.datas.entries.Companion.datalist
import com.example.android.unscramble.ui.game.datas.entries.Companion.username

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class LevelsActivity : AppCompatActivity() {
    val welcome : String by lazy{
        "வணக்கம்  "
    }
    lateinit var adapter: CustomAdapter
    var userscore: Int=0
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val tempscore = it.data?.extras?.getInt("value")
            if ( tempscore !=null && entries.scorecompanion < tempscore)
            entries.scorecompanion = tempscore
        }
    }
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_levels)
         val welcometext: TextView = findViewById(R.id.welcome)
         val sharedPref = this?.getSharedPreferences("sharing", Context.MODE_PRIVATE) ?: return
         username = sharedPref.getString("username", "") ?: ""
         welcometext.setText(welcome + username + "...")
         userscore = sharedPref.getInt(username, 0)
             if (counter == 0) {
             entries.datalist.add(LevelsViewModel(Color.GREEN, "நிறங்கள்    ", true))
             entries.datalist.add(LevelsViewModel(Color.GREEN, "உணவு பொருட்கள்", false))
             entries.datalist.add(LevelsViewModel(Color.GREEN, "உடல் பாகங்கள்        ", false))
             entries.datalist.add(LevelsViewModel(Color.parseColor("#FFCC00"),
                 "உயிரினங்கள்",
                 false))
             entries.datalist.add(LevelsViewModel(Color.parseColor("#FFCC00"),
                 "பழங்கள் மற்றும் காய்கறிகள் ",
                 false))
             entries.datalist.add(LevelsViewModel(Color.parseColor("#FFCC00"),
                 "வீட்டு உபயோக பொருட்கள்",
                 false))
             entries.datalist.add(LevelsViewModel(Color.RED, "தமிழ் இலக்கியம்", false))
             entries.datalist.add(LevelsViewModel(Color.RED, "அரசியல் அறிவியல்", false))
             entries.datalist.add(LevelsViewModel(Color.RED, "கணினி அறிவியல்", false))
             entries.counter+=1
         }
         val recyclerview = findViewById<RecyclerView>(R.id.recycler_view)
         recyclerview.layoutManager = LinearLayoutManager(this)
         adapter = CustomAdapter(this,entries.datalist)
         recyclerview.adapter= adapter

     }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onStart() {
         super.onStart()
         entries.scorecompanion=userscore
         GlobalScope.launch(Dispatchers.Main)
        {
            delay(300)
            val textView = findViewById<TextView>(R.id.highscore)
            var counter = 1
            for ( i in 200..entries.scorecompanion step 200)
            {
                settinglock(counter)
                counter+=1
            }
            textView.text = "அதிக மதிப்பெண்:" + entries.scorecompanion.toString()
   }
   }

    fun functioncallfromadapter(data : String)
    {
        val intent: Intent = Intent(this,MainActivity::class.java)
         intent.putExtra("levels",data)
         startForResult.launch(intent)
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_share -> {
            CustomDialog(this@LevelsActivity,userscore, username).show()
            true
        }
        R.id.action_exit -> {
            val builder = MaterialAlertDialogBuilder(this,R.style.AlertDialog_Theme)
            with(builder)
            {
                setTitle(R.string.app_name)
                setMessage("நீங்கள் பயன்பாட்டிலிருந்து வெளியேற விரும்புகிறீர்களா")
                setPositiveButton("ஆம்",{_,_->
                   finishAffinity()
                })
                setNegativeButton("இல்லை",{_,_->builder.setCancelable(true)}
                )
                show()

            }
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    fun settinglock( index: Int)
    {
        datalist[index].lockkeyopen=true
        adapter.notifyItemChanged(index)
    }

    override fun onBackPressed()
    {
        finishAffinity()
    }
}