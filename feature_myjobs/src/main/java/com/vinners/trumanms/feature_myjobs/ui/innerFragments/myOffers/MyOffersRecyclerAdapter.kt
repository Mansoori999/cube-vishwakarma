package com.vinners.trumanms.feature_myjobs.ui.innerFragments.myOffers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.data.models.jobs.Application
import com.vinners.trumanms.feature_job.ui.jobList.jobDetails.JobDetailsActivity
import com.vinners.trumanms.feature_myjobs.R

interface MyOffersOnClickListener{
    fun onAcceptOffer(application: Application)
    fun onJobClick(application: Application)
}

class MyOffersRecyclerAdapter constructor(
    private val appInfo: AppInfo
) : RecyclerView.Adapter<MyOffersRecyclerAdapter.MyOffersViewHolder>() {

   private var myOffersJobList = listOf<Application>()
   private var myOffersOnClickListener: MyOffersOnClickListener? = null

    fun setMyOfferJobList(myOffersJobList: List<Application>) {
        this.myOffersJobList = myOffersJobList
        notifyDataSetChanged()
    }

    fun setMyOfferClickListener(myOffersOnClickListener: MyOffersOnClickListener){
        this.myOffersOnClickListener = myOffersOnClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOffersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_offers_recycler_items, parent, false)
        return MyOffersViewHolder(view)
    }

    override fun getItemCount(): Int {
       return myOffersJobList.size
    }

    override fun onBindViewHolder(holder: MyOffersViewHolder, position: Int) {
        holder.onBind(myOffersJobList[position])
    }

    inner class MyOffersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val companyLogo = itemView.findViewById<ImageView>(R.id.companyIcon)
        private val firmName = itemView.findViewById<TextView>(R.id.companyHeading)
        private val jobTitle = itemView.findViewById<TextView>(R.id.jobTitle)
        private val jobLocation = itemView.findViewById<TextView>(R.id.companyLoca)
        private val jobSalary = itemView.findViewById<TextView>(R.id.salary)
        private val motorBike = itemView.findViewById<ImageView>(R.id.motorBike)
        private val certificate = itemView.findViewById<ImageView>(R.id.certificate)
        private val workFromHome = itemView.findViewById<ImageView>(R.id.workFormHome)
        private val acceptOfferBtn = itemView.findViewById<Button>(R.id.acceptBtn)


        init {
            acceptOfferBtn.setOnClickListener {
                myOffersOnClickListener?.onAcceptOffer(myOffersJobList[adapterPosition])
            }
            itemView.setOnClickListener {
                myOffersOnClickListener?.onJobClick(myOffersJobList[adapterPosition])
            }
        }


        fun onBind(application: Application){
            firmName.text = application.client
            jobTitle.text = application.title
            jobLocation.text = "${application.cityName}, ${application.state}"

            if (application.twowheelermust)
                motorBike.setVisibilityVisible()
            else
                motorBike.setVisibilityGone()
            if (application.certificateissue)
                certificate.setVisibilityVisible()
            else
                certificate.setVisibilityGone()
            if (application.workfromhome)
                workFromHome.setVisibilityVisible()
            else
                workFromHome.setVisibilityGone()

            if (!application.logo.isNullOrEmpty())
                companyLogo.load(appInfo.getFullAttachmentUrl(application.logo!!))
            else
                companyLogo.load(R.drawable.company)

            if (application.earningmode.equals(JobDetailsActivity.TASK_MODE_PER_TASK,true))
                jobSalary.text = "\u20B9${application.earningpertask} Per Task"
            else if (application.earningmode.equals(JobDetailsActivity.TASK_MODE_ON_PERIOD,true))
                jobSalary.text = "${application.minSalary}-${application.maxSalary} PM"
            else
                jobSalary.text = ""
        }
    }
}