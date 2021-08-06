package com.vinners.cube_vishwakarma.ui.dashboard

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.data.models.homeScreen.MainActivityListModel


class MainActivityRecyclerAdapter(private var clickListener: ClickListener) : RecyclerView.Adapter<MainActivityRecyclerAdapter.MainActivityRecyclerHolder>(){
    private var homeList = listOf<MainActivityListModel>()
    private lateinit var context: Context


    fun updateViewList (homeList: List<MainActivityListModel>){
        this.homeList = homeList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityRecyclerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_layout, parent, false)
        return MainActivityRecyclerHolder(view)
    }

    override fun getItemCount(): Int {
       return homeList.size
    }

    override fun onBindViewHolder(holder: MainActivityRecyclerHolder, position: Int) {
        holder.onBind(homeList[position])
    }

    inner class MainActivityRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        private val name = itemView.findViewById<TextView>(R.id.dashboard_text)

        private  val colorcontainer = itemView.findViewById<CardView>(R.id.dashboard_container)
        init {
            itemView.setOnClickListener(this)
        }

        fun onBind(homeList : MainActivityListModel){
            name.text = homeList.getTitle()
            colorcontainer.setCardBackgroundColor(Color.parseColor(homeList.getColor()))

        }
        override fun onClick(v: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                clickListener.onItemClick(position)
            }
        }
    }

    interface ClickListener {
        fun onItemClick(position: Int)
    }


}


