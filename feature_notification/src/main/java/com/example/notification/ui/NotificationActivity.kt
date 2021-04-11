package com.example.notification.ui

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.notification.R
import com.example.notification.databinding.ActivityNotificationBinding
import com.example.notification.di.DaggerNotificationComponent
import com.example.notification.di.NotificationViewModuleFactory
import com.vinners.trumanms.core.EndlessRecyclerViewOnScrollListener
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.core.taskState.Lse
import com.vinners.trumanms.data.models.notification.Notification
import javax.inject.Inject

class NotificationActivity :
    BaseActivity<ActivityNotificationBinding, NotificationViewModel>(R.layout.activity_notification),
    NotificationClickListener {

    @Inject
    lateinit var viewModelFactory: NotificationViewModuleFactory

    override val viewModel: NotificationViewModel by viewModels {
        viewModelFactory
    }

    private val notificationAdapter: NotificationRecyclerAdapter by lazy {
        NotificationRecyclerAdapter().apply {
            setListInteractionListener(this@NotificationActivity)
        }
    }

    override fun onInitDependencyInjection() {
        DaggerNotificationComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.recyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.recyclerView.setHasFixedSize(true)
        viewBinding.recyclerView.isMotionEventSplittingEnabled = false
        viewBinding.recyclerView.adapter = notificationAdapter
        val linearLayoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.layoutManager = linearLayoutManager
        viewBinding.recyclerView.addOnScrollListener(setupScrollListener(linearLayoutManager))
        viewBinding.swipeRefreshNotification.setOnRefreshListener {
            notificationAdapter.removeAll()
              viewModel.getNotification("0")
            viewBinding.swipeRefreshNotification.isRefreshing = false
        }
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupScrollListener(layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener {
        return object : EndlessRecyclerViewOnScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.getNotification(page.toString())
            }
        }
    }

    override fun onInitViewModel() {
        viewModel.notificationState.observe(this, Observer {
            when (it) {
                    Lce.Loading -> {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        if (notificationAdapter.isEmpty)
                            viewBinding.progressBar.setVisibilityVisible()
                        else {
                            notificationAdapter.removeErrorView()
                            notificationAdapter.addLoadingView()
                        }
                    }
                is Lce.Content -> {
                    viewBinding.progressBar.setVisibilityGone()
                    if (notificationAdapter.isEmpty && it.content.isEmpty()) {
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = " No Notification Found"
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        notificationAdapter.removeLoadingView()
                        notificationAdapter.addItems(it.content)
                    }
                }
                is Lce.Error -> {
                    viewBinding.progressBar.setVisibilityGone()
                    if (notificationAdapter.isEmpty) {
                        viewBinding.errorLayout.root.setVisibilityVisible()
                        viewBinding.errorLayout.infoImageIv.load(R.drawable.ic_information)
                        viewBinding.errorLayout.errorActionButton.setVisibilityGone()
                        viewBinding.errorLayout.messageTv.text = it.error
                    } else {
                        viewBinding.errorLayout.root.setVisibilityGone()
                        notificationAdapter.removeLoadingView()
                        notificationAdapter.addErrorView(it.error)
                    }
                }
            }
        })
        viewModel.readNotificationState.observe(this, Observer {
            when(it){
                Lse.Loading -> {

                }
                is Lse.Success -> {
                        Toast.makeText(this,"Notification Read Successfully",Toast.LENGTH_SHORT).show()
                }
                is Lse.Error -> {
                    showInformationDialog(it.error)
                }
            }
        })
        viewModel.getNotification("0")
    }

    override fun onNotificationItemClicked(notification: Notification) {
       if (notification.isRead.not())
           showNotificationDialog(notification)
    }

    fun showNotificationDialog(notification: Notification){
        val dialogView = layoutInflater.inflate(R.layout.notification_detail_dialog, null)
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setView(dialogView)
        val notificationDate = dialogView.findViewById<TextView>(R.id.notifiDetailDateTime)
        val notificationMessage = dialogView.findViewById<TextView>(R.id.notifiDetailMessage)
        notificationDate.text = notification.createdon
        notificationMessage.text = notification.message
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "okay") { _, _ ->
            if (notification.isRead.not()) {
                notificationAdapter.removeAll()
                viewModel.getNotification("0")
            }
            alertDialog.cancel()
        }
        //  alertDialog.setButton(
        // AlertDialog.BUTTON_NEUTRAL,"Go To Task",
        // DialogInterface.OnClickListener { dialogInterface, i ->
        //openJobDescription(notification)
        // alertDialog.cancel()
        // })
        alertDialog.show()
        viewModel.readNotification("read",notification.id.toString())
    }

    override fun onRetryButtonClicked() {

    }
}
