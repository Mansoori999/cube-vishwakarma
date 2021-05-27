package com.vinners.cube_vishwakarma.ui.tutorials

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.UrlQuerySanitizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.data.models.tutorials.TutorialsResponse


class TutorialsRecyclerAdapter : RecyclerView.Adapter<TutorialsRecyclerAdapter.TutorialsRecyclerHolder>() {
    private var tutorialList = listOf<TutorialsResponse>()
    private lateinit var context: Context




    fun updateViewList (tutorialList: List<TutorialsResponse>){
        this.tutorialList = tutorialList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorialsRecyclerHolder {
        this.context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tutorials_list_layout, parent, false)
        return TutorialsRecyclerHolder(view)
    }


    override fun onBindViewHolder(holder: TutorialsRecyclerHolder, position: Int) {
        holder.onBind(tutorialList[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val url = tutorialList.get(position).path
            intent.setData(Uri.parse(url))
            context.startActivity(intent)
        }

    }
    override fun getItemCount(): Int {
        return tutorialList.size
    }




    inner class TutorialsRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        private val name = itemView.findViewById<TextView>(R.id.name)
        private val description = itemView.findViewById<TextView>(R.id.description)
        private val path = itemView.findViewById<ImageView>(R.id.path)


        init {

//            itemView.setOnClickListener {
//                outletsClickListener.OnOutletClick(mFilteredItemList[adapterPosition])
//            }
        }

        fun onBind(tutorialList: TutorialsResponse){

            val sanitizer: UrlQuerySanitizer = UrlQuerySanitizer()
            sanitizer.setAllowUnregisteredParamaters(true)
            sanitizer.parseUrl(tutorialList.path)
            val urlpath  = sanitizer.getValue("v")

            name.text = "${tutorialList.name}"
            description.text = "${tutorialList.description}"

            path.load("https://img.youtube.com/vi/${urlpath}/mqdefault.jpg")


        }
    }




}