package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.DateTimeHelper
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.outlets.OutletsList
import com.vinners.cube_vishwakarma.ui.outlets.OutletRecyclerAdapter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


interface  AllComplaintsClickListener {

    fun OnAllComplaintsClick(myComplaintList: MyComplaintList)
}

class AllComplaintRecyclerAdapter() : RecyclerView.Adapter<AllComplaintRecyclerAdapter.AllComplaintRecyclerHolder>(), Filterable {
    private var complaintList = listOf<MyComplaintList>()
    private lateinit var context: Context
    private lateinit var allComplaintsClickListener: AllComplaintsClickListener
    var mFilteredItemList = listOf<MyComplaintList>()
    private var searchFilter: SearchFilters? = null


    fun updateViewList (complaintList: List<MyComplaintList>){
        this.complaintList = complaintList
        mFilteredItemList = complaintList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllComplaintRecyclerHolder {
        this.context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_complaint_layout, parent, false)
        return AllComplaintRecyclerHolder(view)
    }

    override fun getItemCount(): Int {
       return mFilteredItemList.size
    }

    override fun onBindViewHolder(holder: AllComplaintRecyclerHolder, position: Int) {
        holder.onBind(mFilteredItemList[position])
    }
    fun setAllComplaintsListener(allComplaintsClickListener: AllComplaintsClickListener) {
        this.allComplaintsClickListener = allComplaintsClickListener
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
                mFilteredItemList = complaintList
            } else {
                val filteredList = mutableListOf<MyComplaintList>()
                for (resultlist in complaintList) {
                    if (resultlist.outlet.isNullOrEmpty().not() || resultlist.complaintid.isNullOrEmpty().not()
                        || resultlist.customercode.isNullOrEmpty().not()) {
                        if (resultlist.outlet!!.toLowerCase().contains(charString.toLowerCase()) ||
                            resultlist.customercode!!.toLowerCase()
                                .contains(charString.toLowerCase()) ||
                            resultlist.complaintid!!.toLowerCase()
                                .contains(charString.toLowerCase())
                        ) {
                            filteredList.add(resultlist)
                        }
                    }else{
                        Toast.makeText(context, "Beach", Toast.LENGTH_SHORT).show()
                    }
                }
                mFilteredItemList = filteredList
            }
            val filterResults = FilterResults()
            filterResults.values = mFilteredItemList
            return filterResults
        }
        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            mFilteredItemList = if(results == null || results.values == null)
                ArrayList<MyComplaintList>()
            else
                results.values as List<MyComplaintList>

            notifyDataSetChanged()

        }
    }
    inner class AllComplaintRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val complaintId = itemView.findViewById<TextView>(R.id.id)
        private val date = itemView.findViewById<TextView>(R.id.dateEt)
        private val  status = itemView.findViewById<TextView>(R.id.status)
        private val name = itemView.findViewById<TextView>(R.id.outlet_name)
//        private val code = itemView.findViewById<TextView>(R.id.customer_code)
        private val letterstatus = itemView.findViewById<TextView>(R.id.letterstatus)

        init {

            itemView.setOnClickListener {
                allComplaintsClickListener.OnAllComplaintsClick(mFilteredItemList[adapterPosition])
            }
        }

        fun onBind(complaintList: MyComplaintList){

            name.text = "${complaintList.outlet}   -   ${complaintList.customercode}"
            complaintId.text = "${complaintList.complaintid}"
//            date.text =  "${complaintList.fordate}"
           date.text = DateTimeHelper.getDDMMYYYYDateFromString(complaintList.fordate!!)
//            code.text = "-  ${complaintList.customercode}"
            letterstatus.text = "Letter status : ${complaintList.letterstatus}"
            status.text = complaintList.status
            if (complaintList.status!!.toLowerCase().equals("done")) {
                status.setTextColor(Color.parseColor("#99CC33"))
            }else if (complaintList.status!!.toLowerCase().equals("due")){
                status.setTextColor(Color.parseColor("#FF6633"))
            }else if (complaintList.status!!.toLowerCase().equals("working")){
                status.setTextColor(Color.parseColor("#0066CC"))
            }else if (complaintList.status!!.toLowerCase().equals("hold")){
                status.setTextColor(Color.parseColor("#6699CC"))
            }else if (complaintList.status!!.toLowerCase().equals("cancelled")){
                status.setTextColor(Color.parseColor("#2EC43E"))
            }else if (complaintList.status!!.toLowerCase().equals("draft")){
                status.setTextColor(Color.parseColor("#996699"))
            }else if (complaintList.status!!.toLowerCase().equals("estimated")){
                status.setTextColor(Color.parseColor("#003366"))
            }else if (complaintList.status!!.toLowerCase().equals("billed")){
                status.setTextColor(Color.parseColor("#FFCC33"))
            }else if (complaintList.status!!.toLowerCase().equals("payment")){
                status.setTextColor(Color.parseColor("#E16117"))
            }
        }
    }




}