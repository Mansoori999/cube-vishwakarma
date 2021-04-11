package com.example.notification.ui

import android.content.Context
import android.graphics.Typeface
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
import com.example.notification.R
import com.vinners.trumanms.core.DateTimeHelper
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.data.models.jobs.Job
import com.vinners.trumanms.data.models.notification.Notification
import com.vinners.trumanms.feature_job.ui.jobList.jobList.JobInteractionListener

interface NotificationClickListener {

    fun onRetryButtonClicked()

    fun onNotificationItemClicked(notification: Notification)
}

class NotificationRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val mJobList: MutableList<Notification>
    private var mListJobInteractionListener: NotificationClickListener? = null
    private var context: Context? = null

    // private var imageLoader: ImageLoader? = null
    private var LOADING_OBJECT = Notification(
        id = null,
        userId = null,
        message = null,
        deliveredon = null,
        isdelivered = false,
        isRead = false,
        createdon = null,
        readon = null
    )
    private var ERROR_OBJECT = Notification(
        id = null,
        userId = null,
        message = null,
        deliveredon = null,
        isdelivered = false,
        isRead = false,
        createdon = null,
        readon = null
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
                .inflate(R.layout.layout_error_retry,
                    parent,
                    false
                )
            ErrorViewHolder(view)
        }
    }

    private fun onGenericItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.notification_recycler_items, parent, false)
        return JobViewHolder(view)
    }

    private fun onBindGenericItemViewHolder(holder: JobViewHolder, position: Int) {
        holder.bindTo(mJobList[position])
    }

    private fun onBindErrorItemViewHolder(holder: ErrorViewHolder, position: Int) {
        holder.bindTo(mJobList[position])
    }

    fun add(item: Notification) {
        add(null, item)
    }

    fun add(position: Int?, item: Notification) {
        if (position != null) {
            mJobList.add(position, item)
            notifyItemInserted(position)
        } else {
            mJobList.add(item)
            notifyItemInserted(mJobList.size - 1)
        }
    }

    fun addItems(itemsList: List<Notification>) {
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
            add( Notification(
                id = null,
                userId = null,
                message = null,
                deliveredon = null,
                isdelivered = false,
                isRead = false,
                createdon = null,
                readon = null
            ))
            return true
        }
        return false
    }

    fun addErrorView(error: String): Boolean {
        if (getItemViewType(mJobList.size - 1) != VIEW_TYPE_ERROR) {
            add( Notification(
                id = null,
                userId = null,
                message = null,
                deliveredon = null,
                isdelivered = false,
                isRead = false,
                createdon = null,
                readon = null
            ))
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

    fun setListInteractionListener(listJobInteractionListener: NotificationClickListener) {
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

        private val message = itemView.findViewById<TextView>(R.id.notifiMessage)
        private val time = itemView.findViewById<TextView>(R.id.notifiDateTime)



        init {
            itemView.setOnClickListener {
                mListJobInteractionListener?.onNotificationItemClicked(mJobList[adapterPosition])
            }
        }

        fun bindTo(notification: Notification) {
            message.text = notification.message
            if (notification.createdon.isNullOrEmpty().not()){
                time.text = DateTimeHelper.getFancyDateWithTimeFromString(notification.createdon)
            }
            if (notification.isRead.not()){
                message.typeface = Typeface.DEFAULT_BOLD
                time.typeface = Typeface.DEFAULT_BOLD
            }else{
                message.typeface = Typeface.DEFAULT
                time.typeface = Typeface.DEFAULT
            }
        }
    }

    inner class ProgressBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val messageTV: TextView =
            itemView.findViewById(com.vinners.trumanms.feature_job.R.id.messageTV)
        private val retryButton: Button =
            itemView.findViewById(com.vinners.trumanms.feature_job.R.id.retryButton)

        init {
            retryButton.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            //  mListJobInteractionListener?.onRetryButtonClicked()
        }

        fun bindTo(item: Notification) {
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