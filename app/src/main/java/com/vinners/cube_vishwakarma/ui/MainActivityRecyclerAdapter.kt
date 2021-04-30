package com.vinners.cube_vishwakarma.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityRecyclerAdapter.MainActivityRecyclerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_screen_layout, parent, false)
        return MainActivityRecyclerHolder(view)
    }

    override fun getItemCount(): Int {
       return homeList.size
    }

    override fun onBindViewHolder(holder: MainActivityRecyclerAdapter.MainActivityRecyclerHolder, position: Int) {
        holder.onBind(homeList[position])
    }

    inner class MainActivityRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView),View.OnClickListener {

        private val name = itemView.findViewById<TextView>(R.id.complaint_label)
        init {
            itemView.setOnClickListener(this)
        }

        fun onBind(homeList : MainActivityListModel){
            name.text = homeList.getTitle()

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


