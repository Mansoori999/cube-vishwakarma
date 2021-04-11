package com.vinners.trumanms.feature.auth.ui.register

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import java.util.*

class CustomerAdapter(
    context: Context,
    val viewResourceId: Int,
    private val itemList: List<String>
) : ArrayAdapter<String>(context, viewResourceId) {
    private val suggestions = mutableListOf<String>()
    private var itemsAll = listOf<String>()
    private var filter: Filter? = null

    init {
        itemsAll = itemList
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(viewResourceId, null)
        }
        val item = itemList[position]
        val textViewLable = view as TextView
        if (textViewLable != null) {
            textViewLable.text = item
        }
        return view!!
    }

    override fun getFilter(): Filter {
        if (filter == null)
            filter = NewFilters()
        return filter!!
    }

    inner class NewFilters : Filter() {
        override fun convertResultToString(resultValue: Any?): CharSequence {
            val value = resultValue as String
            return value
        }

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterValue = constraint.toString()
            if (filterValue.indexOf("@") != -1) {
                val domain = filterValue.substring(filterValue.indexOf("@"))
                var address : String? = null
                try {
                    address = filterValue.substring(0,filterValue.indexOf("@"))
                }catch (e: Exception){
                    address = ""
                }
                suggestions.clear()
                itemsAll.forEach {
                    if (it.toLowerCase(Locale.ROOT).startsWith(domain.toLowerCase(Locale.ROOT))){
                        suggestions.add(address+it)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                return filterResults
            }else
                return FilterResults()
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values != null) {
                val filteredList = results?.values as List<String>
                if (results.count > 0) {
                    clear()
                    filteredList.forEach {
                        add(it)
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }
}