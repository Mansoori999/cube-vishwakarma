package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.DateTimeHelper
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.ui.outlets.OutletDetalisActivity
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
        holder.name.setOnClickListener {
            val intent = Intent(context, OutletDetalisActivity::class.java)
            intent.putExtra(OutletDetalisActivity.OUTLET_ID,mFilteredItemList.get(position).outletid)
            context.startActivity(intent)
        }

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

        private val work = itemView.findViewById<TextView>(R.id.work)
        private val date = itemView.findViewById<TextView>(R.id.complaintidAnddateEt)
        private val  status = itemView.findViewById<TextView>(R.id.status)
        val name = itemView.findViewById<TextView>(R.id.outlet_name)
        private val salesArea = itemView.findViewById<TextView>(R.id.salesAreaEt)
        private val ro = itemView.findViewById<TextView>(R.id.ro)
        private val subAdmin = itemView.findViewById<TextView>(R.id.am)
        private val orderby = itemView.findViewById<TextView>(R.id.orderby)

        init {

            itemView.setOnClickListener {
                allComplaintsClickListener.OnAllComplaintsClick(mFilteredItemList[adapterPosition])
            }


        }

        fun onBind(complaintList: MyComplaintList) {

            name.text = "${complaintList.outlet} - ${complaintList.outletcategory}"
            ro.text = "${complaintList.regionaloffice}"

            salesArea.text = "${complaintList.salesarea}"
            work.text = "${complaintList.work}"
            subAdmin.text = "${complaintList.subadmin}"
            orderby.text =  "${complaintList.orderby}"
           date.text = "${complaintList.complaintid} - ${DateTimeHelper.getDDMMYYYYDateFromString(complaintList.fordate!!)}"
//            code.text = "-  ${complaintList.customercode}"
          //  letterstatus.text = "Letter status : ${complaintList.letterstatus}"
            status.text = complaintList.status
            if (complaintList.status!!.toLowerCase().equals("done")) {
                status.setTextColor(Color.parseColor("#99CC33"))
            }else if (complaintList.status!!.toLowerCase().equals("due")){
                status.setTextColor(Color.parseColor("#FFA500"))
            }else if (complaintList.status!!.toLowerCase().equals("working")){
                status.setTextColor(Color.parseColor("#0066CC"))
            }else if (complaintList.status!!.toLowerCase().equals("hold")){
                status.setTextColor(Color.parseColor("#FF6633"))
            }else if (complaintList.status!!.toLowerCase().equals("cancelled")){
                status.setTextColor(Color.parseColor("#2EC43E"))
            }else if (complaintList.status!!.toLowerCase().equals("draft")){
                status.setTextColor(Color.parseColor("#996699"))
            }else if (complaintList.status!!.toLowerCase().equals("estimated")){
                status.setTextColor(Color.parseColor("#003366"))
            }else if (complaintList.status!!.toLowerCase().equals("billed")){
                status.setTextColor(Color.parseColor("#FFCC33"))
            }else if (complaintList.status!!.toLowerCase().equals("payment")){
                status.setTextColor(Color.parseColor("#FFCC33"))
            }else if (complaintList.status!!.toLowerCase().equals("pending letter")){
                status.setTextColor(Color.parseColor("#FF6633"))
            }
        }
    }




}

