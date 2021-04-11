package com.vinners.trumanms.feature_myjobs.ui.innerFragments.inHandJobs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.data.models.jobs.Application
import com.vinners.trumanms.feature_job.ui.jobList.jobDetails.JobDetailsActivity
import com.vinners.trumanms.feature_myjobs.R
interface InHandJobClickListener{
    fun onCloseClick(application: Application)

    fun onDailyNotesClick(application: Application)

    fun onStatusChange(application: Application)

}
class InHandJobRecyclerAdapter constructor(
    private val appInfo: AppInfo
) : RecyclerView.Adapter<InHandJobRecyclerAdapter.InHandVewHolder>() {

    var inHandJobsList = listOf<Application>()
    lateinit var inHandJobClickListener: InHandJobClickListener

    fun setInHandJobList(inHandJobsList: List<Application>){
        this.inHandJobsList = inHandJobsList
        notifyDataSetChanged()
    }

    fun setListener(inHandJobClickListener: InHandJobClickListener){
        this.inHandJobClickListener = inHandJobClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InHandVewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.in_hand_recycler_items, parent, false)
        return InHandVewHolder(view)
    }

    override fun getItemCount(): Int {
        return inHandJobsList.size
    }

    override fun onBindViewHolder(holder: InHandVewHolder, position: Int) {
        holder.onBind(inHandJobsList[position])
    }

    inner class InHandVewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val companyNameTV: TextView = itemView.findViewById(R.id.company_name_tv)
        private val jobPositionTV: TextView = itemView.findViewById(R.id.job_position_tv)
        private val jobLocationTV: TextView = itemView.findViewById(R.id.job_place)
        private val logo = itemView.findViewById<ImageView>(R.id.companyIcon)
        private val jobSalary = itemView.findViewById<TextView>(R.id.salary)
        private val motorBike = itemView.findViewById<ImageView>(R.id.motorBike)
        private val certificate = itemView.findViewById<ImageView>(R.id.certificate)
        private val workFromHome = itemView.findViewById<ImageView>(R.id.workFormHome)
        private val closeBtn = itemView.findViewById<ImageView>(R.id.closeBtn)
        private val dailyNotesBtn = itemView.findViewById<Button>(R.id.dailyNotesBtn)
        private val status = itemView.findViewById<TextView>(R.id.workStatus)
        private val changeBtn = itemView.findViewById<Button>(R.id.jobStatusChngBtn)

        init {
            closeBtn.setOnClickListener {
                inHandJobClickListener.onCloseClick(inHandJobsList[adapterPosition])
            }
            dailyNotesBtn.setOnClickListener {
                inHandJobClickListener.onDailyNotesClick(inHandJobsList[adapterPosition])
            }
            changeBtn.setOnClickListener {
                inHandJobClickListener.onStatusChange(inHandJobsList[adapterPosition])
            }
        }
        fun onBind(application: Application){
            companyNameTV.text = application.client
            jobPositionTV.text = application.title
            status.text = application.jobStatus
            jobLocationTV.text = "${application.cityName}, ${application.state}"
            if (application.logo.isNullOrEmpty().not())
                logo.load(appInfo.getFullAttachmentUrl(application.logo!!))
            else
                logo.load(R.drawable.company)

            if (application.isTrained.not() || application.jobStatus.equals("finish",true))
                changeBtn.setVisibilityGone()
            else
                changeBtn.setVisibilityVisible()

                motorBike.isVisible = application.twowheelermust
                certificate.isVisible = application.certificateissue
                workFromHome.isVisible = application.workfromhome


            if (application.earningmode.equals(JobDetailsActivity.TASK_MODE_PER_TASK,true))
                jobSalary.text = "\u20B9${application.earningpertask} Per Task"
            else if (application.earningmode.equals(JobDetailsActivity.TASK_MODE_ON_PERIOD,true))
                jobSalary.text = "${application.minSalary}-${application.maxSalary} PM"
            else
                jobSalary.text = ""
        }
    }
}