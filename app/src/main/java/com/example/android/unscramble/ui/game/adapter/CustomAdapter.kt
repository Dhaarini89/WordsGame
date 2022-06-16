package com.example.android.unscramble.ui.game.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.android.unscramble.MainActivity
import com.example.android.unscramble.R
import com.example.android.unscramble.ui.game.LevelsActivity
import com.example.android.unscramble.ui.game.datas.LevelsViewModel
import com.example.android.unscramble.ui.game.datas.entries
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.nio.file.Files.delete
import kotlin.contracts.contract

class CustomAdapter(val appcontext: Context,val mylist : List<LevelsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
       val view = LayoutInflater.from(parent.context)
           .inflate(R.layout.levels_design,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val DataListModel = mylist[position]
        holder.textView.text = DataListModel.text
        holder.layoutView.setBackgroundColor(DataListModel.layoutcolor)
        holder.layoutView.setOnClickListener{
           if (holder.imageView.visibility == View.INVISIBLE) {
               (appcontext as LevelsActivity).functioncallfromadapter(holder.textView.text.trim() as String)
           }
            else {
               MaterialAlertDialogBuilder(appcontext)
                   .setTitle(R.string.app_name)
                   .setMessage("முந்தைய நிலைகளை முடித்த பின்னரே இந்த நிலையைத் திறக்க முடியும்")
                   .setPositiveButton("சரி", null)
                   .show()
                    }
            
        }

        if (DataListModel.lockkeyopen == true)
        {
           holder.imageView.visibility = View.INVISIBLE
       }
    }


    override fun getItemCount(): Int {
       return mylist.size
    }

    class ViewHolder(dataView : View) : RecyclerView.ViewHolder(dataView)
    {
        val layoutView : RelativeLayout = dataView.findViewById(R.id.layout)
        val imageView : ImageView =dataView.findViewById(R.id.image_lock)
        val textView : TextView = dataView.findViewById(R.id.text)

    }
}