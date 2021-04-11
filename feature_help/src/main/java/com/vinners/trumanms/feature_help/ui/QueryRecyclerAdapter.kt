package com.vinners.trumanms.feature_help.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.DateTimeHelper
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.data.models.help.Query
import com.vinners.trumanms.feature_help.R
import de.hdodenhof.circleimageview.CircleImageView

interface QueryClickListener {
    fun onQueryClick(query: Query)
}

class QueryRecyclerAdapter constructor(
    private val appInfo: AppInfo
) : RecyclerView.Adapter<QueryRecyclerAdapter.QueryViewHolder>() {

    private var queryList = listOf<Query>()
    lateinit var queryClickListener: QueryClickListener

    fun updateLsit(queryList: List<Query>) {
        this.queryList = queryList
        notifyDataSetChanged()
    }

    fun setListener(queryClickListener: QueryClickListener){
        this.queryClickListener = queryClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.query_recycler_items, parent, false)
        return QueryViewHolder(view)
    }

    override fun onBindViewHolder(holder: QueryViewHolder, position: Int) {
        holder.bindTo(queryList[position])
    }

    override fun getItemCount(): Int {
        return queryList.size
    }

    inner class QueryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val question = itemView.findViewById<TextView>(R.id.queEt)
        private val answer = itemView.findViewById<TextView>(R.id.ansEt)
        private val category = itemView.findViewById<TextView>(R.id.category)
        private val queImg = itemView.findViewById<CircleImageView>(R.id.queImg)
        private val date = itemView.findViewById<TextView>(R.id.dateEt)

        init {
            queImg.setOnClickListener {
                queryClickListener.onQueryClick(queryList[adapterPosition])
            }
        }

        fun bindTo(query: Query) {
            category.text = query.category
            question.text = "Q. ${query.question}"
            if (query.answer.isNullOrEmpty().not())
                answer.text = "A. ${query.answer}"
            if (query.path.isNullOrEmpty().not())
                queImg.load(appInfo.getFullAttachmentUrl(query.path!!))
            else
                queImg.setVisibilityGone()

            if (query.forDate.isNullOrEmpty().not())
                date.text = DateTimeHelper.getFancyDateFromString(query.forDate)
        }
    }


}