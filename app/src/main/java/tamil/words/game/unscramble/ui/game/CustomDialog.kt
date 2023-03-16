package tamil.words.game.unscramble.ui.game
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import tamil.words.game.unscramble.R
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
            cachePath.mkdir()
            val file = File(cachePath, "sample.png")

            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.close()

            var urifile = FileProvider.getUriForFile(context,
                "tamil.words.game.unscramble.fileprovider",
                file)
            Log.d("urifile", urifile.toString())
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, urifile)
                type = "image/png"

            }
            var chooser : Intent =Intent.createChooser(shareIntent, null)
            var resInfoList : List<ResolveInfo> = context.packageManager.queryIntentActivities(chooser,PackageManager.MATCH_DEFAULT_ONLY)
            for (resolveinfo :ResolveInfo in resInfoList)
            {
                val packagename = resolveinfo.activityInfo.packageName
                context.grantUriPermission(packagename,urifile,Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            }
            context.startActivity(chooser)
        }

    }
}
