package com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinners.trumanms.data.models.stateAndCity.WorkCategory
import com.vinners.trumanms.feature.auth.R

interface WorkCategoryClickListener {
    fun onWorkCategoryClick(workCategory: WorkCategory)
}

class WorkCategoryRecyclerAdapter :
    RecyclerView.Adapter<WorkCategoryRecyclerAdapter.WorkCategoryViewHolder>() {
    private var workCategoryList = listOf<WorkCategory>()
    private var workCategoryClickListener: WorkCategoryClickListener? = null
    private var isBoxChecked: Boolean? = false

    fun updateList(workCategoryList: List<WorkCategory>) {
        this.workCategoryList = workCategoryList.sortedBy {
            it.work
        }
        notifyDataSetChanged()
    }

    fun setClickListener(workCategoryClickListener: WorkCategoryClickListener) {
        this.workCategoryClickListener = workCategoryClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.multi_select_recycler_items, parent, false)
        return WorkCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkCategoryViewHolder, position: Int) {
        holder.onBind(workCategoryList[position])
    }

    override fun getItemCount(): Int {
        return workCategoryList.size
    }

    inner class WorkCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val language = itemView.findViewById<TextView>(R.id.valueEt)
        private val checkbox = itemView.findViewById<CheckBox>(R.id.selectedCheckbox)

        init {
            checkbox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    if (isChecked) {
                        workCategoryList[adapterPosition].isBoxChecked = true
                    } else {
                        workCategoryList[adapterPosition].isBoxChecked = false
                    }
                    workCategoryClickListener?.onWorkCategoryClick(workCategoryList[adapterPosition])
                }
            })
        }

        fun onBind(workCategory: WorkCategory) {
            language.text = workCategory.work
            if (workCategory.isBoxChecked)
                checkbox.isChecked = true
            else
                checkbox.isChecked = false
        }
    }
}