package com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinners.trumanms.data.models.stateAndCity.City
import com.vinners.trumanms.feature.auth.R

interface CityOnClickListener {
    fun onCityClick(city: City)
}
class CityRecyclerAdapter :RecyclerView.Adapter<CityRecyclerAdapter.CityViewHolder>(),
    Filterable {

    private var oldCityList = listOf<City>()
    private var newCityList = listOf<City>()
    private var cityOnClickListener: CityOnClickListener? = null
    private var stateFilter: StateFilter? = null

    fun updateList(cityList: List<City>) {
        this.oldCityList = cityList
        this.newCityList = cityList
        notifyDataSetChanged()
    }

    fun setCityOnClickListener(cityOnClickListener: CityOnClickListener) {
        this.cityOnClickListener = cityOnClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.state_recycler_item, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bindTo(newCityList[position])
    }

    override fun getItemCount(): Int {
        return newCityList.size
    }

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val stateEt = itemView.findViewById<TextView>(R.id.stateEt)

        init {
            itemView.setOnClickListener {
                cityOnClickListener?.onCityClick(newCityList[adapterPosition])
            }
        }

        fun bindTo(city: City) {
            stateEt.text = city.cityName
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
                newCityList = oldCityList
            else {
                newCityList = oldCityList.filter {
                    it.cityName.toLowerCase().contains(searchString.toLowerCase())
                }
            }
            val filterResult = FilterResults()
            filterResult.values = newCityList
            return filterResult
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            newCityList = results?.values as List<City>
            notifyDataSetChanged()
        }
    }
}