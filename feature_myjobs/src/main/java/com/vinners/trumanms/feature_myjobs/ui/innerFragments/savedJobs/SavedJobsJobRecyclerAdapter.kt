package com.vinners.trumanms.feature_myjobs.ui.innerFragments.savedJobs

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.data.models.jobs.AppliedJob
import com.vinners.trumanms.data.models.jobs.SavedJob
import com.vinners.trumanms.feature_job.ui.jobList.jobDetails.JobDetailsActivity
import com.vinners.trumanms.feature_myjobs.R
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.appliedJobs.AppliedJobsJobRecyclerAdapterListener

class SavedJobsJobRecyclerAdapter constructor(
    private val appInfo: AppInfo
) : RecyclerView.Adapter<SavedJobsJobRecyclerAdapter.InHandVewHolder>() {

    private var jobs: List<SavedJob> = emptyList()
    private val viewBinderHelper = ViewBinderHelper()
    private lateinit var mSavedJobRecyclerAdapterListener: SavedJobRecyclerAdapterListener


    fun updateList(jobs: List<SavedJob>) {
        this.jobs = jobs
        notifyDataSetChanged()
    }

    fun setOnClickListener(savedJobRecyclerAdapterListener: SavedJobRecyclerAdapterListener){
        this.mSavedJobRecyclerAdapterListener = savedJobRecyclerAdapterListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InHandVewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_saved_job, parent, false)
        return InHandVewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobs.size
    }

    override fun onBindViewHolder(holder: InHandVewHolder, position: Int) {
        val job = jobs[position]
   //     viewBinderHelper.bind(holder.swipeRevealLayout, job.jobId)
        holder.bindTo(job)
    }

    inner class InHandVewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) ,    View.OnClickListener{

   //     val swipeRevealLayout: SwipeRevealLayout = itemView.findViewById(R.id.swipeRevealLayout)

        private val companyLogoIV: ImageView = itemView.findViewById(R.id.companyIcon)
        private val companyNameTV: TextView = itemView.findViewById(R.id.company_name_tv)
        private val jobPositionTV: TextView = itemView.findViewById(R.id.job_position_tv)
        private val jobLocationTV: TextView = itemView.findViewById(R.id.job_place)

        private val needBikeForJobIV: ImageView = itemView.findViewById(R.id.motorBike)
        private val needCertificationIV: ImageView = itemView.findViewById(R.id.certificate)
        private val canWorkFromHomeIV: ImageView = itemView.findViewById(R.id.workFormHome)

        private val appliedDayEstTV: TextView = itemView.findViewById(R.id.dateOfOpening)
        private val amountCanEarnTV: TextView = itemView.findViewById(R.id.amount_can_earn_TV)

//        private val shareJobLayout: View = itemView.findViewById(R.id.shareJobLayout)
//        private val applyJobLayout: View = itemView.findViewById(R.id.apply_this_job_layout)

        init {
            itemView.setOnClickListener(this)
//            shareJobLayout.setOnClickListener(this)
//            applyJobLayout.setOnClickListener(this)
        }

        fun bindTo(job: SavedJob) = job.apply {
            if (companyIcon.isNullOrEmpty().not()) {
                val fullUrl = appInfo.getFullAttachmentUrl(companyIcon!!)
                companyLogoIV.load(Uri.parse(fullUrl))
            } else {
                companyLogoIV.load(R.drawable.company)
            }
            companyNameTV.text = companyName
            jobPositionTV.text = title
            jobLocationTV.text = "${cityName}, ${job.state}"
//            amountCanEarnTV.text = "${job.minSalary}-${job.maxSalary} PM"
            needBikeForJobIV.isVisible = bikeRequired
            needCertificationIV.isVisible = certificationRequired
            canWorkFromHomeIV.isVisible = canWorkFromHome
            appliedDayEstTV.text = appliedDateEstimate

            if (job.earningmode.equals(JobDetailsActivity.TASK_MODE_PER_TASK,true))
                amountCanEarnTV.text = "\u20B9${job.earningpertask} Per Task"
            else if (job.earningmode.equals(JobDetailsActivity.TASK_MODE_ON_PERIOD,true))
                amountCanEarnTV.text = "${job.minSalary}-${job.maxSalary} PM"
            else
                amountCanEarnTV.text = ""
        }


        override fun onClick(v: View?) {
            val clickedView = v ?: return

            val position = adapterPosition
            val job = jobs[position]
            mSavedJobRecyclerAdapterListener.onJobClick(job)
//            when (clickedView.id) {
//                R.id.shareJobLayout -> mSavedJobRecyclerAdapterListener.shareJobClicked(
//                    position,
//                    job
//                )
//                R.id.apply_this_job_layout -> mSavedJobRecyclerAdapterListener.applyJobClicked(
//                    position,
//                    job
//                )
//            }
        }
    }


}

interface SavedJobRecyclerAdapterListener {

    fun shareJobClicked(
        position: Int,
        savedJob: SavedJob
    )

    fun applyJobClicked(
        position: Int,
        savedJob: SavedJob
    )

   fun onJobClick(savedJob: SavedJob)

}