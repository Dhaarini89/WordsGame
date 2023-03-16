package tamil.words.game.unscramble.ui.game.adapter

import android.content.Context
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import tamil.words.game.unscramble.ui.game.LevelsActivity
import tamil.words.game.unscramble.ui.game.datas.LevelsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import tamil.words.game.unscramble.R

class CustomAdapter(val appcontext: Context,val mylist : List<LevelsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
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