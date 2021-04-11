package com.vinners.trumanms.feature.wallet.ui.ui.transactionHistory

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import coil.api.load
import com.google.android.material.tabs.TabLayout
import com.vinners.trumanms.core.base.BaseActivity
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.money.Transaction
import com.vinners.trumanms.feature.wallet.R
import com.vinners.trumanms.feature.wallet.databinding.ActivityTranctionHistoryBinding
import com.vinners.trumanms.feature.wallet.ui.di.DaggerWalletComponent
import com.vinners.trumanms.feature.wallet.ui.di.WalletViewModelFactory
import com.vinners.trumanms.feature.wallet.ui.ui.WalletInfoViewModel
import javax.inject.Inject

class TranctionHistoryActivity :
    BaseActivity<ActivityTranctionHistoryBinding, WalletInfoViewModel>(R.layout.activity_tranction_history) {
    private var isCreditList: Boolean = false
    private var isDebitList: Boolean = false

    @Inject
    lateinit var viewModelFactory: WalletViewModelFactory
    override val viewModel: WalletInfoViewModel by viewModels {
        viewModelFactory
    }
    private val transactionRecyclerAdapter: TranctionHistoryRecyclerAdapter by lazy {
        TranctionHistoryRecyclerAdapter().apply {
            updateList(emptyList())
        }
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            if (viewBinding.transactionTabLayout.selectedTabPosition == 0) {
                isCreditList = false
                isDebitList = false
            } else if (viewBinding.transactionTabLayout.selectedTabPosition == 1) {
                isCreditList = true
                isDebitList = false
            } else if (viewBinding.transactionTabLayout.selectedTabPosition == 2) {
                isCreditList = false
                isDebitList = true
            }
            viewModel.getTransaction()
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {

        }

    }

    override fun onInitDependencyInjection() {
        DaggerWalletComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.transactionHistoryRecycler.itemAnimator = DefaultItemAnimator()
        viewBinding.transactionHistoryRecycler.adapter = transactionRecyclerAdapter
        viewBinding.transactionRefreshLayout.setOnRefreshListener {
            viewModel.getTransaction()
            viewBinding.transactionRefreshLayout.isRefreshing = false
        }
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
        viewBinding.transactionTabLayout.addTab(
            viewBinding.transactionTabLayout.newTab().setText("All")
        )
        viewBinding.transactionTabLayout.addTab(
            viewBinding.transactionTabLayout.newTab().setText("Credit")
        )
        viewBinding.transactionTabLayout.addTab(
            viewBinding.transactionTabLayout.newTab().setText("Debit")
        )
        viewBinding.transactionTabLayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    override fun onInitViewModel() {
        viewModel.transactionState.observe(this, Observer {
            when (it) {
                Lce.Loading -> jobsLoading()
                is Lce.Content -> jobsLoaded(it.content)
                is Lce.Error -> errorWhileLoadingJobs(it.error)
            }
        })
        viewModel.getTransaction()
    }

    private fun jobsLoading() {
        viewBinding.jobsInfoLayout.root.setVisibilityGone()
        transactionRecyclerAdapter.updateList(emptyList())
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityVisible()
    }

    private fun jobsLoaded(transaction: List<Transaction>) {
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityGone()
        if (transaction.isEmpty()) {
            transactionRecyclerAdapter.updateList(transaction)
            showNoJobsFoundLayout()
        } else {
            viewBinding.jobsInfoLayout.root.setVisibilityGone()
            if (isCreditList){
              val newTransaction =  transaction.filter {
                    it.type.equals("credit",true)
                }
                transactionRecyclerAdapter.updateList(newTransaction)
            } else if (isDebitList){
                val newTransaction = transaction.filter {
                    it.type.equals("debit",true)
                }
                transactionRecyclerAdapter.updateList(newTransaction)
            }else
                transactionRecyclerAdapter.updateList(transaction)
        }
    }

    private fun showNoJobsFoundLayout() {
        viewBinding.jobsInfoLayout.root.setVisibilityVisible()

        viewBinding.jobsInfoLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.jobsInfoLayout.messageTv.text = "No Transaction Found"
        viewBinding.jobsInfoLayout.errorActionButton.setVisibilityGone()
    }

    private fun errorWhileLoadingJobs(error: String) {
        viewBinding.jobsLoadingCenterProgressBar.setVisibilityGone()
        transactionRecyclerAdapter.updateList(emptyList())
        viewBinding.jobsInfoLayout.root.setVisibilityVisible()

        viewBinding.jobsInfoLayout.infoImageIv.load(R.drawable.ic_information)
        viewBinding.jobsInfoLayout.messageTv.text = error
        viewBinding.jobsInfoLayout.errorActionButton.text = "Re-try"
        viewBinding.jobsInfoLayout.errorActionButton.setOnClickListener {
            viewModel.getTransaction()
        }
    }
}