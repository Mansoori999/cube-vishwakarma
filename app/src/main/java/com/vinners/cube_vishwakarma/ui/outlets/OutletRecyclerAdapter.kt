package com.vinners.cube_vishwakarma.ui.outlets

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.DateTimeHelper
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityGone
import com.vinners.cube_vishwakarma.core.extensions.setVisibilityVisible
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


interface  OutletsClickListener {

    fun OnOutletClick(outletsList: OutletsList)
}

class OutletRecyclerAdapter() : RecyclerView.Adapter<OutletRecyclerAdapter.OutletRecyclerHolder>(), Filterable {
    private var outletList = listOf<OutletsList>()
    private lateinit var context: Context
    private lateinit var outletsClickListener: OutletsClickListener
    var mFilteredItemList = listOf<OutletsList>()
    private var searchFilter: SearchFilters? = null
    private var gps : String ? = null


    fun updateViewList (outletList: List<OutletsList>){
        this.outletList = outletList
        mFilteredItemList = outletList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutletRecyclerHolder {
        this.context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.outlet_list_layout, parent, false)
        return OutletRecyclerHolder(view)
    }


    override fun onBindViewHolder(holder: OutletRecyclerHolder, position: Int) {
        holder.onBind(mFilteredItemList[position])

    }
    override fun getItemCount(): Int {
        return mFilteredItemList.size
    }
    fun setOutletListener(outletsClickListener: OutletsClickListener) {
        this.outletsClickListener = outletsClickListener
    }

    override fun getFilter(): Filter {
        if (searchFilter == null) {
            searchFilter = SearchFilters()
        }
        return searchFilter as SearchFilters
    }
    inner class SearchFilters : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val charString = constraint.toString()
            if (charString.isEmpty()) {
                mFilteredItemList = outletList
            } else {
                val filteredList = mutableListOf<OutletsList>()
                for (resultlist in outletList) {
                    if (resultlist.name!!.toLowerCase().contains(charString.toLowerCase())   ||
                            resultlist.regionaloffice!!.toLowerCase().contains(charString.toLowerCase())||
                            resultlist.salesarea!!.toLowerCase().contains(charString.toLowerCase()) ||
                            resultlist.customercode!!.toLowerCase().contains(charString.toLowerCase())||
                            resultlist.districtname!!.toLowerCase().contains(charString.toLowerCase())) {
                        filteredList.add(resultlist)
                    }
                }
                mFilteredItemList = filteredList
            }
            val filterResults = FilterResults()
            filterResults.values = mFilteredItemList
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            mFilteredItemList = results?.values as List<OutletsList>
            notifyDataSetChanged()

        }
    }
    inner class OutletRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        private val name = itemView.findViewById<TextView>(R.id.name)
        private val code = itemView.findViewById<TextView>(R.id.code)
        private val district = itemView.findViewById<TextView>(R.id.district)
        private val sales = itemView.findViewById<TextView>(R.id.sales)
        private var gpslocation = itemView.findViewById<ImageView>(R.id.gps)
        private val  regionalOffice = itemView.findViewById<TextView>(R.id.regional_office)

        init {

            itemView.setOnClickListener {
                outletsClickListener.OnOutletClick(mFilteredItemList[adapterPosition])
            }
            val mapUrl = "http://maps.google.co.in/maps?q="+ gps
            gpslocation.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.co.in/maps?q="+ gps))
                context.startActivity(intent)
            }
        }

        fun onBind(outletsList: OutletsList){

            name.text = "${outletsList.name}"
            code.text = "${outletsList.customercode}"
            district.text = "${outletsList.districtname}"
            sales.text = outletsList.salesarea
            regionalOffice.text = outletsList.regionaloffice
            if (outletsList.gps != null && outletsList.gps != "," ) {
                gpslocation.setVisibilityVisible()
                gps = outletsList.gps
            }else{
                gpslocation.setVisibilityGone()
            }


        }
    }




}