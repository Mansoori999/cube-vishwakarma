package com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.DateTimeHelper
import com.vinners.cube_vishwakarma.data.models.complaints.MyComplaintList
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


interface  AllComplaintsClickListener {

    fun OnAllComplaintsClick(myComplaintList: MyComplaintList)
}

class AllComplaintRecyclerAdapter() : RecyclerView.Adapter<AllComplaintRecyclerAdapter.AllComplaintRecyclerHolder>() {
    private var complaintList = listOf<MyComplaintList>()
    private lateinit var context: Context
    private lateinit var simpleDateFormat: SimpleDateFormat
    private lateinit var allComplaintsClickListener: AllComplaintsClickListener


    fun updateViewList (complaintList: List<MyComplaintList>){
        this.complaintList = complaintList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllComplaintRecyclerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_complaint_layout, parent, false)
        return AllComplaintRecyclerHolder(view)
    }

    override fun getItemCount(): Int {
       return complaintList.size
    }

    override fun onBindViewHolder(holder: AllComplaintRecyclerHolder, position: Int) {
        holder.onBind(complaintList[position])
    }
    fun setAllComplaintsListener(allComplaintsClickListener: AllComplaintsClickListener) {
        this.allComplaintsClickListener = allComplaintsClickListener
    }



    inner class AllComplaintRecyclerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val complaintId = itemView.findViewById<TextView>(R.id.id)
        private val date = itemView.findViewById<TextView>(R.id.dateEt)
        private val  status = itemView.findViewById<TextView>(R.id.status)
        private val name = itemView.findViewById<TextView>(R.id.outlet_name)
        private val code = itemView.findViewById<TextView>(R.id.customer_code)
        private val letterstatus = itemView.findViewById<TextView>(R.id.letterstatus)

        init {

            itemView.setOnClickListener {
                allComplaintsClickListener.OnAllComplaintsClick(complaintList[adapterPosition])
            }
        }

        fun onBind(complaintList: MyComplaintList){

            name.text = "${complaintList.outlet}"
            complaintId.text = "${complaintList.complaintid}"
//            date.text =  "${complaintList.fordate}"
           date.text = DateTimeHelper.getDDMMYYYYDateFromString(complaintList.fordate!!)
            code.text = "${complaintList.customercode}"
            letterstatus.text = "Letter status : ${complaintList.letterstatus}"
            status.text = complaintList.status
            if (complaintList.status!!.toLowerCase().equals("done")) {
                status.setTextColor(Color.parseColor("#FF8C00"))
            }else if (complaintList.status!!.toLowerCase().equals("due")){
                status.setTextColor(Color.parseColor("#008000"))
            }else if (complaintList.status!!.toLowerCase().equals("working")){
                status.setTextColor(Color.parseColor("#FF5733"))
            }else if (complaintList.status!!.toLowerCase().equals("hold")){
                status.setTextColor(Color.parseColor("#DDE40D"))
            }else if (complaintList.status!!.toLowerCase().equals("cancelled")){
                status.setTextColor(Color.parseColor("#2EC43E"))
            }else if (complaintList.status!!.toLowerCase().equals("draft")){
                status.setTextColor(Color.parseColor("#2E4EC4"))
            }else if (complaintList.status!!.toLowerCase().equals("estimated")){
                status.setTextColor(Color.parseColor("#B627B1"))
            }else if (complaintList.status!!.toLowerCase().equals("billed")){
                status.setTextColor(Color.parseColor("#F03791"))
            }else if (complaintList.status!!.toLowerCase().equals("payment")){
                status.setTextColor(Color.parseColor("#E16117"))
            }
        }
    }




}