package com.example.android.unscramble.ui.game


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.example.android.unscramble.R
import com.example.android.unscramble.ui.game.datas.entries.Companion.username
import java.io.File
import java.io.FileOutputStream


class CustomDialog(context: Context, score: Int, username: String) : Dialog(context) {

    val c_score: Int
    val c_username: String

    init {
        setCancelable(true)
        c_score = score
        c_username = username
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)
        val textView: TextView = findViewById(R.id.text_username)
        textView.text = "வாழ்த்துக்கள் " + c_username
        val textView_score: TextView = findViewById(R.id.text_score)
        textView_score.text = "உங்கள் மதிப்பெண்:" + c_score.toString()
        val imagestar3: ImageView = findViewById(R.id.star3)
        val imagestar4: ImageView = findViewById(R.id.star4)
        val imagestar5: ImageView = findViewById(R.id.star5)
        if (c_score < 1800) {
            imagestar5.setColorFilter(Color.GRAY)
        }
        if (c_score < 1200) {
            imagestar4.setColorFilter(Color.GRAY)
        }
        if (c_score < 600) {
            imagestar3.setColorFilter(Color.GRAY)
        }
        val button: Button = findViewById(R.id.share)
        button.setOnClickListener {
            val content: LinearLayout = findViewById(R.id.outerlayout)
            val bitmap: Bitmap =
                Bitmap.createBitmap(content.width, content.height, Bitmap.Config.ARGB_8888)
            val canvas: Canvas = Canvas(bitmap)
            content.draw(canvas)
            var cachePath = File(context.getCacheDir(), "Pictures")
            Log.d("cachepath", cachePath.path.toString())
            val file = File(cachePath, "example.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.close()
            Log.d("file", file.path.toString())
            var urifile = FileProvider.getUriForFile(context,
                "com.example.android.unscramble.fileprovider",
                file)
            Log.d("urifile", urifile.toString())
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, urifile)
                type = "image/png"

            }
            shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(Intent.createChooser(shareIntent, null))
        }

    }
}
