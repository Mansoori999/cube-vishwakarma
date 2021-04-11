package com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinners.trumanms.data.models.stateAndCity.Pincode
import com.vinners.trumanms.feature.auth.R

interface PincodeOnClickListener {
    fun onPincodeClick(pincode: Pincode)
}
class PincodeRecyclerAdapter : RecyclerView.Adapter<PincodeRecyclerAdapter.PincodeViewHolder>(),
    Filterable {

    private var oldPincodeList = listOf<Pincode>()
    private var newPincodeList = listOf<Pincode>()
    private var pincodeOnClickListener: PincodeOnClickListener? = null
    private var pincodeFilter: PincodeFilter? = null

    fun updateList(pincodeList: List<Pincode>) {
        this.oldPincodeList = pincodeList
        this.newPincodeList = pincodeList
        notifyDataSetChanged()
    }

    fun setPincodeOnClickListener(pincodeOnClickListener: PincodeOnClickListener) {
        this.pincodeOnClickListener = pincodeOnClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PincodeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.state_recycler_item, parent, false)
        return PincodeViewHolder(view)
    }

    override fun onBindViewHolder(holder: PincodeViewHolder, position: Int) {
        holder.bindTo(newPincodeList[position])
    }

    override fun getItemCount(): Int {
        return newPincodeList.size
    }

    inner class PincodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val stateEt = itemView.findViewById<TextView>(R.id.stateEt)

        init {
            itemView.setOnClickListener {
                pincodeOnClickListener?.onPincodeClick(newPincodeList[adapterPosition])
            }
        }

        fun bindTo(pincode: Pincode) {
            stateEt.text = pincode.pincode
        }
    }

    override fun getFilter(): Filter {
        if (pincodeFilter == null)
            pincodeFilter = PincodeFilter()
        return pincodeFilter!!
    }

    inner class PincodeFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val searchString = constraint.toString()
            if (searchString.isEmpty())
                newPincodeList = oldPincodeList
            else {
                newPincodeList = newPincodeList.filter {
                    it.pincode.toLowerCase().contains(searchString.toLowerCase())
                }
            }
            val filterResult = FilterResults()
            filterResult.values = newPincodeList
            return filterResult
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            newPincodeList = results?.values as List<Pincode>
            notifyDataSetChanged()
        }
    }

}