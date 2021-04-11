package com.example.feature_profile.ui.jobHistory

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.feature_profile.R
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.data.models.jobHistory.JobHistory
import com.vinners.trumanms.data.models.jobs.Job

interface JobInteractionListener {
    fun onJobListItemClick(job: JobHistory)
}
class JobHistoryViewRecyclerAdapter constructor(
    private val appInfo: AppInfo
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val mJobList: MutableList<JobHistory>
    private var mListJobInteractionListener: JobInteractionListener? = null
    private var context: Context? = null

    // private var imageLoader: ImageLoader? = null
    private var LOADING_OBJECT = JobHistory(
        jobId = null,
        logo = null
    )
    private var ERROR_OBJECT = JobHistory(
       jobId = null,
        logo = null
    )

    @ViewType
    var viewType: Int = VIEW_TYPE_LIST
    val isEmpty: Boolean get() = itemCount == 0

    init {
        mJobList = mutableListOf()
        viewType = VIEW_TYPE_LIST
        // mListJobInteractionListener = null
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            mJobList[position] == LOADING_OBJECT -> VIEW_TYPE_LOADING
            mJobList[position] == ERROR_OBJECT -> VIEW_TYPE_ERROR
            else -> viewType
        }
    }

    fun getNextPageIndex(): Int {
        return mJobList.size / PAGE_SIZE
    }

    override fun getItemCount(): Int {
        return mJobList.size
    }

    override fun getItemId(position: Int): Long {
        return if (mJobList.size >= position)
            mJobList[position].hashCode().toLong()
        else
            -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return if (viewType == VIEW_TYPE_LOADING || viewType == VIEW_TYPE_ERROR) {
            onIndicationViewHolder(parent, viewType)
        } else
            onGenericItemViewHolder(parent)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            holder.itemViewType == VIEW_TYPE_LOADING -> return  // no-op
            holder.itemViewType == VIEW_TYPE_ERROR -> onBindErrorItemViewHolder(
                holder as ErrorViewHolder,
                position
            )
            else -> onBindGenericItemViewHolder(holder as JobViewHolder, position)
        }
    }

    private fun onIndicationViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_LOADING) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_progress_bar, parent, false)
            ProgressBarViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_error_retry, parent, false)
            ErrorViewHolder(view)
        }
    }

    private fun onGenericItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.job_history_recycler_items, parent, false)
        return JobViewHolder(view)
    }

    private fun onBindGenericItemViewHolder(holder: JobViewHolder, position: Int) {
        holder.bindTo(mJobList[position])
    }

    private fun onBindErrorItemViewHolder(holder: ErrorViewHolder, position: Int) {
        holder.bindTo(mJobList[position])
    }

    fun add(item: JobHistory) {
        add(null, item)
    }

    fun add(position: Int?, item: JobHistory) {
        if (position != null) {
            mJobList.add(position, item)
            notifyItemInserted(position)
        } else {
            mJobList.add(item)
            notifyItemInserted(mJobList.size - 1)
        }
    }

    fun addItems(itemsList: List<JobHistory>) {
        val previousCount = itemCount
        mJobList.addAll(itemsList)
        notifyItemRangeInserted(previousCount, mJobList.size - 1)
    }

    fun remove(position: Int) {
        if (mJobList.size < position) {
            Log.w(TAG, "The item at position: $position doesn't exist")
            return
        }
        mJobList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeAll() {
        mJobList.clear()
        notifyDataSetChanged()
    }

    fun addLoadingView(): Boolean {
        if (getItemViewType(mJobList.size - 1) != VIEW_TYPE_LOADING) {
            add(
                LOADING_OBJECT
            )
            return true
        }
        return false
    }

    fun addErrorView(error: String): Boolean {
        if (getItemViewType(mJobList.size - 1) != VIEW_TYPE_ERROR) {
            add(ERROR_OBJECT)
            return true
        }
        return false
    }

    fun removeLoadingView(): Boolean {
        if (mJobList.size > 1) {
            val loadingViewPosition = mJobList.size - 1
            if (getItemViewType(loadingViewPosition) == VIEW_TYPE_LOADING) {
                remove(loadingViewPosition)
                return true
            }
        }
        return false
    }

    fun isLoadingViewShown(): Boolean {
        if (mJobList.size > 1) {
            val loadingViewPosition = mJobList.size - 1
            return getItemViewType(loadingViewPosition) == VIEW_TYPE_LOADING

        }
        return false
    }

    fun removeErrorView(): Boolean {
        if (mJobList.size > 1) {
            val errorViewPosition = mJobList.size - 1
            if (getItemViewType(errorViewPosition) == VIEW_TYPE_ERROR) {
                remove(errorViewPosition)
                return true
            }
        }
        return false
    }

    fun setListInteractionListener(listJobInteractionListener: JobInteractionListener) {
        mListJobInteractionListener = listJobInteractionListener
    }

    /*  fun getImageLoader(imageLoader: ImageLoader) {
          this.imageLoader = imageLoader
      }*/

    @IntDef(
        VIEW_TYPE_LOADING,
        VIEW_TYPE_LIST
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class ViewType

    /**
     * Interface for handling list interactions
     */

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val companyLogo = itemView.findViewById<ImageView>(R.id.companyIcon)
        private val firmName = itemView.findViewById<TextView>(R.id.companyHeading)
        private val jobId = itemView.findViewById<TextView>(R.id.jobId)
        private val jobLocation = itemView.findViewById<TextView>(R.id.jobLocation)
        private val workDuration = itemView.findViewById<TextView>(R.id.workDuration)
        private val workStatus = itemView.findViewById<TextView>(R.id.workStatus)
        private val jobDescrirtion = itemView.findViewById<TextView>(R.id.jobDescription)


        init {
            itemView.setOnClickListener {
                mListJobInteractionListener?.onJobListItemClick(mJobList[adapterPosition])
            }
        }

        fun bindTo(job: JobHistory) {
            firmName.text = job.clientName
            jobLocation.text = "${job.city}, India"
            workDuration.text = "Work Duration: ${job.workDuration} days"
            workStatus.text = job.status
            jobId.text = job.jobNo
            jobDescrirtion.text = job.description


            if (!job.logo.isNullOrBlank())
                companyLogo.load(appInfo.getFullAttachmentUrl(job.logo!!))
        }

    }

    inner class ProgressBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val messageTV: TextView = itemView.findViewById(R.id.messageTV)
        private val retryButton: Button = itemView.findViewById(R.id.retryButton)

        init {
            retryButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            //  mListJobInteractionListener?.onRetryButtonClicked()
        }

        fun bindTo(item: JobHistory) {
            // messageTV.text = item.title
        }
    }

    companion object {
        const val TAG = "JobsListAdapter"
        const val VIEW_TYPE_LIST = 1
        const val VIEW_TYPE_LOADING = 2
        const val VIEW_TYPE_ERROR = 3

        private const val PAGE_SIZE = 20
    }
}