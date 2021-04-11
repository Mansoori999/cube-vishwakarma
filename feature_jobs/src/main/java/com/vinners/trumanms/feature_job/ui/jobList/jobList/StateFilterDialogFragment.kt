package com.vinners.trumanms.feature_job.ui.jobList.jobList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.vinners.trumanms.base.AppInfo
import com.vinners.trumanms.core.base.BaseDialogFragment
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.stateAndCity.State
import com.vinners.trumanms.feature_job.R
import com.vinners.trumanms.feature_job.databinding.FragmentStateFilterBinding
import com.vinners.trumanms.feature_job.di.DaggerJobsComponent
import com.vinners.trumanms.feature_job.di.JobsViewModelFactory
import java.lang.StringBuilder
import javax.inject.Inject

class StateFilterDialogFragment :
    BaseDialogFragment<FragmentStateFilterBinding, JobsViewModel>(R.layout.fragment_state_filter),
    StateFiterClickListener {

    @Inject
    lateinit var viewModelFactory: JobsViewModelFactory

    @Inject
    lateinit var appInfo: AppInfo
    lateinit var onStateSubmitListener: OnStateSubmitListener
    private var checkedItemList = mutableListOf<String>()
    private val languageItemsWrapper = StringBuilder()
    private var selectedLanguageItems: String? = null

    override val viewModel: JobsViewModel by viewModels { viewModelFactory }

    private val stateRecyclerFilterAdapter: StateRecyclerFilterAdapter by lazy {
        StateRecyclerFilterAdapter().apply {
            updateList(emptyList())
            setClickListener(this@StateFilterDialogFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            selectedLanguageItems = it.getString(INTENT_EXTRA_SELECTED_ITEM) ?: return@let
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {

            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onInitDependencyInjection() {
        DaggerJobsComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.stateRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.stateRecyclerView.adapter = stateRecyclerFilterAdapter
        viewBinding.okBtn.setOnClickListener {
            languageItemsWrapper.setLength(0)
            for (i in checkedItemList.indices) {
                languageItemsWrapper.append(checkedItemList.get(i))
                if (i < checkedItemList.size.minus(1))
                    languageItemsWrapper.append(",")
            }
            onStateSubmitListener.onStatesSubmit(languageItemsWrapper)
            dismiss()
        }
    }

    override fun onInitViewModel() {
        viewModel.state.observe(this, Observer {
            when(it){
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    val workCatList = it.content
                    if (selectedLanguageItems.isNullOrEmpty().not()) {
                        val selectedWorkItemList = selectedLanguageItems?.split(",")
                        selectedWorkItemList?.forEach {
                            val workItem = it
                            checkedItemList.add(workItem)
                            workCatList.forEach {
                                if (it.stateId.equals(workItem,true))
                                    it.isChecked = true
                            }
                        }
                    }
                    stateRecyclerFilterAdapter.updateList(it.content)
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.getState()
    }

    interface OnStateSubmitListener {
        fun onStatesSubmit(statesId: StringBuilder)
    }

    companion object {
        const val TAG = "stateFilterDialogFragment"
        const val INTENT_EXTRA_SELECTED_ITEM = "selected_items"
        const val CITY = "City"
        const val NOT_IN_LIST = "Not In List"

        fun launchDialogFragment(
            fragmentManager: FragmentManager,
            selectedItems: String?,
            onStateSubmitListener: OnStateSubmitListener
        ) {
            val fragment = StateFilterDialogFragment()
            fragment.arguments = bundleOf(INTENT_EXTRA_SELECTED_ITEM to selectedItems)
            fragment.onStateSubmitListener = onStateSubmitListener
            fragment.show(fragmentManager, TAG)
        }
    }

    override fun onStateClick(state: State) {
        if (state.isChecked && checkedItemList.contains(state.stateId).not())
            checkedItemList.add(state.stateId!!)
        else if (state.isChecked.not() && checkedItemList.contains(state.stateId))
            checkedItemList.remove(state.stateId)
    }
}