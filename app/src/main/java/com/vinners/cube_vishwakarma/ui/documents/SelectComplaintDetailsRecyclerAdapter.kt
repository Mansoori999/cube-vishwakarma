package com.vinners.cube_vishwakarma.ui.documents

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
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintRequestResponse
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complain.AllComplaintRecyclerAdapter
import mobile.fitbitMerch.ui.masterData.ComplaintSelectionListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*




class SelectComplaintDetailsRecyclerAdapter() : RecyclerView.Adapter<SelectComplaintDetailsRecyclerAdapter.SelectComplaintDetailsAdapterHolder>(),Filterable {
    private var complaintList = listOf<MyComplaintList>()
    private lateinit var context: Context
    private lateinit var complaintsClickListener: ComplaintSelectionListener
    var mFilteredItemList = listOf<MyComplaintList>()
    private var searchFilter: SearchFilters? = null

    fun updateViewList (complaintList: List<MyComplaintList>){
        this.complaintList = complaintList
        mFilteredItemList = complaintList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectComplaintDetailsAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row_complaints, parent, false)
        return SelectComplaintDetailsAdapterHolder(view)
    }

    override fun getItemCount(): Int {
       return mFilteredItemList.size
    }

    override fun onBindViewHolder(holder: SelectComplaintDetailsAdapterHolder, position: Int) {
        holder.onBind(mFilteredItemList[position])
    }
    fun setComplaintsListener(complaintsClickListener: ComplaintSelectionListener) {
        this.complaintsClickListener = complaintsClickListener
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

    inner class SelectComplaintDetailsAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val complaintno = itemView.findViewById<TextView>(R.id.compaints_id_textview)
        private val  status = itemView.findViewById<TextView>(R.id.complaints_status_textview)
        private val name = itemView.findViewById<TextView>(R.id.complaints_outlet_name_textview)
        private val workby = itemView.findViewById<TextView>(R.id.complaints_work_textview)
        private val date = itemView.findViewById<TextView>(R.id.complaints_date_textview)
        private val letterstatus = itemView.findViewById<TextView>(R.id.letter_status_textview)
        private val engName = itemView.findViewById<TextView>(R.id.engineer_name_textview)
        private val supervisorAllocated = itemView.findViewById<TextView>(R.id.complaints_allocated_to_supervisor_textview)
        private val subadminAllocated = itemView.findViewById<TextView>(R.id.complaints_allocated_to_subadmin_textview)
        private val FielduserAllocated = itemView.findViewById<TextView>(R.id.complaints_allocated_fielduser_to_textview)




        init {

            itemView.setOnClickListener {
                complaintsClickListener.onComplaintSelected(complaintList[adapterPosition])
            }
        }

        fun onBind(complaintList: MyComplaintList){
            complaintno.text = complaintList.complaintid
            name.text = "${complaintList.outlet}   -   ${complaintList.customercode}"
            workby.text = "${complaintList.work}"
           date.text = DateTimeHelper.getDDMMYYYYDateFromString(complaintList.fordate!!)
            letterstatus.text = complaintList.letterstatus
            supervisorAllocated.text = complaintList.supervisor
            subadminAllocated.text = complaintList.subadmin

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