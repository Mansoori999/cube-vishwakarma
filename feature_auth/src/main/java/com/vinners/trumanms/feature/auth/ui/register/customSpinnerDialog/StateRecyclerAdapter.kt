package com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinners.trumanms.data.models.stateAndCity.State
import com.vinners.trumanms.feature.auth.R

interface StateOnClickListener {
    fun onStateClick(state: State)
}

class StateRecyclerAdapter : RecyclerView.Adapter<StateRecyclerAdapter.StateViewHolder>(),
    Filterable {

    private var oldstateList = listOf<State>()
    private var newStateList = listOf<State>()
    private var stateOnClickListener: StateOnClickListener? = null
    private var stateFilter: StateFilter? = null

    fun updateList(stateList: List<State>) {
        this.oldstateList = stateList.sortedBy {
            it.stateName
        }
        this.newStateList = stateList.sortedBy {
            it.stateName
        }
        notifyDataSetChanged()
    }

    fun setStateOnClickListener(stateOnClickListener: StateOnClickListener) {
        this.stateOnClickListener = stateOnClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.state_recycler_item, parent, false)
        return StateViewHolder(view)
    }

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        holder.bindTo(newStateList[position])
    }

    override fun getItemCount(): Int {
        return newStateList.size
    }

    inner class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val stateEt = itemView.findViewById<TextView>(R.id.stateEt)

        init {
                itemView.setOnClickListener {
                    stateOnClickListener?.onStateClick(newStateList[adapterPosition])
                }
        }

        fun bindTo(state: State) {
            stateEt.text = state.stateName
        }
    }

    override fun getFilter(): Filter {
        if (stateFilter == null)
            stateFilter = StateFilter()
        return stateFilter!!
    }

    inner class StateFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val searchString = constraint.toString()
            if (searchString.isEmpty())
                newStateList = oldstateList
            else {
                newStateList = oldstateList.filter {
                    it.stateName.toLowerCase().contains(searchString.toLowerCase())
                }
            }
            val filterResult = FilterResults()
            filterResult.values = newStateList
            return filterResult
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            newStateList = results?.values as List<State>
            notifyDataSetChanged()
        }

    }
}