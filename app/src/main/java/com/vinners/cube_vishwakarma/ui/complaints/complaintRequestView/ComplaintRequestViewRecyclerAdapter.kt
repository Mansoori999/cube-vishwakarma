package com.vinners.cube_vishwakarma.ui.complaints.complaintRequestView

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.DateTimeHelper
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import com.vinners.cube_vishwakarma.data.models.complaints.complaintRequest.ComplaintRequestResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


interface  ComplaintRequestViewClickListener {

    fun OnComplaintRequestClick(myComplaintList: ComplaintRequestResponse)
}

class ComplaintRequestViewRecyclerAdapter() : RecyclerView.Adapter<ComplaintRequestViewRecyclerAdapter.ComplaintRequestViewAdapterHolder>() {
    private var complaintList = listOf<ComplaintRequestResponse>()
    private lateinit var context: Context
    private lateinit var complaintRequestViewClickListener: ComplaintRequestViewClickListener

    fun updateViewList (complaintList: List<ComplaintRequestResponse>){
        this.complaintList = complaintList

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintRequestViewAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.complaint_request_view_layout, parent, false)
        return ComplaintRequestViewAdapterHolder(view)
    }

    override fun getItemCount(): Int {
       return complaintList.size
    }

    override fun onBindViewHolder(holder: ComplaintRequestViewAdapterHolder, position: Int) {
        holder.onBind(complaintList[position])
    }
    fun setComplaintsListener(complaintRequestViewClickListener: ComplaintRequestViewClickListener) {
        this.complaintRequestViewClickListener = complaintRequestViewClickListener
    }



    inner class ComplaintRequestViewAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val workby = itemView.findViewById<TextView>(R.id.workby)
        private val date = itemView.findViewById<TextView>(R.id.dateEt)
        private val  status = itemView.findViewById<TextView>(R.id.status)
        private val name = itemView.findViewById<TextView>(R.id.outlet_name)
//        private val code = itemView.findViewById<TextView>(R.id.customer_code)
        private val district = itemView.findViewById<TextView>(R.id.district)

        init {

            itemView.setOnClickListener {
                complaintRequestViewClickListener.OnComplaintRequestClick(complaintList[adapterPosition])
            }
        }

        fun onBind(complaintList: ComplaintRequestResponse){

            name.text = "${complaintList.outlet}   -   ${complaintList.customercode}"
            workby.text = "${complaintList.zone}"
//            date.text =  "${complaintList.fordate}"
           date.text = DateTimeHelper.getDDMMYYYYDateFromString(complaintList.fordate!!)
//            code.text = "-  ${complaintList.customercode}"
            district.text = "${complaintList.district}"
            status.text = complaintList.status
            if (complaintList.status!!.toLowerCase().equals("approved")) {
                status.setTextColor(Color.parseColor("#008000"))
            }else if (complaintList.status!!.toLowerCase().equals("pending")){
                status.setTextColor(Color.parseColor("#FF8C00"))
            }else if (complaintList.status!!.toLowerCase().equals("rejected")){
                status.setTextColor(Color.parseColor("#0066CC"))
            }
        }
    }




}