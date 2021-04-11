package com.vinners.trumanms.feature_job.ui.jobList.jobList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinners.trumanms.data.models.stateAndCity.State
import com.vinners.trumanms.feature_job.R

interface StateFiterClickListener {
    fun onStateClick(state: State)
}

class StateRecyclerFilterAdapter :
    RecyclerView.Adapter<StateRecyclerFilterAdapter.StateViewHolder>() {

    private var stateList = listOf<State>()
    private var stateFiterClickListener: StateFiterClickListener? = null
    private var isBoxChecked: Boolean? = false

    fun updateList(stateList: List<State>) {
        this.stateList = stateList.sortedBy {
            it.stateName
        }
        notifyDataSetChanged()
    }

    fun setClickListener(stateFiterClickListener: StateFiterClickListener) {
        this.stateFiterClickListener = stateFiterClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.multi_select_recycler_items, parent, false)
        return StateViewHolder(view)
    }

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        holder.onBind(stateList[position])
    }

    override fun getItemCount(): Int {
        return stateList.size
    }

    inner class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val stateName = itemView.findViewById<TextView>(R.id.valueEt)
        private val checkbox = itemView.findViewById<CheckBox>(R.id.selectedCheckbox)

        init {
            checkbox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    if (isChecked) {
                        stateList[adapterPosition].isChecked = true
                    } else {
                        stateList[adapterPosition].isChecked = false
                    }
                    stateFiterClickListener?.onStateClick(stateList[adapterPosition])
                }
            }
            )
        }

        fun onBind(state: State) {
            stateName.text = state.stateName
            if (state.isChecked)
                checkbox.isChecked = true
            else
                checkbox.isChecked = false
        }
    }
}