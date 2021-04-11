package com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.vinners.trumanms.core.base.BaseDialogFragment
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.stateAndCity.WorkCategory
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.FragmentWorkCatDialogBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import javax.inject.Inject
import kotlin.text.StringBuilder

class WorkCategoryDialogFragment :
    BaseDialogFragment<FragmentWorkCatDialogBinding, StateCityViewModel>(
        R.layout.fragment_work_cat_dialog
    ), WorkCategoryClickListener {
    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory
    lateinit var onDialogClickListener: OnDialogClickListener
    private var checkedItemList = mutableListOf<String>()
    val workCatItemsWrapper = StringBuilder()
    private var selectedWorkItems: String? = null

    override val viewModel: StateCityViewModel by viewModels {
        viewModelFactory
    }

    private val workCategoryRecyclerAdapter: WorkCategoryRecyclerAdapter by lazy {
        WorkCategoryRecyclerAdapter().apply {
            updateList(emptyList())
            setClickListener(this@WorkCategoryDialogFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            selectedWorkItems = it.getString(INTENT_EXTRA_SELECTED_ITEMS) ?: return@let
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onInitDependencyInjection() {
        DaggerAuthComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.workCatRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.workCatRecyclerView.adapter = workCategoryRecyclerAdapter
        viewBinding.okBtn.setOnClickListener {
            workCatItemsWrapper.setLength(0)
            for (i in checkedItemList.indices) {
                workCatItemsWrapper.append(checkedItemList.get(i))
                if (i < checkedItemList.size.minus(1))
                    workCatItemsWrapper.append(",")
            }
            onDialogClickListener.onWorkCatClick(workCatItemsWrapper)
            dismiss()
        }
    }

    override fun onInitViewModel() {
        viewModel.workCategory.observe(this, androidx.lifecycle.Observer {
            when (it) {
                Lce.Loading -> {

                }
                is Lce.Content -> {
                    val workCatList = it.content
                    if (selectedWorkItems.isNullOrEmpty().not()) {
                        val selectedWorkItemList = selectedWorkItems?.split(",")
                      selectedWorkItemList?.forEach {
                          val workItem = it
                          checkedItemList.add(workItem)
                          workCatList.forEach {
                              if (it.work.equals(workItem,true))
                                  it.isBoxChecked = true
                          }
                      }
                    }
                    workCategoryRecyclerAdapter.updateList(workCatList)
                }
                is Lce.Error -> {

                }
            }
        })
        viewModel.getWorkCategory()
    }
    companion object{
        const val TAG = "workCategoryDialogFragment"
        const val INTENT_EXTRA_SELECTED_ITEMS = "selected_items"
        const val WORK_CATEGORY = "Work Category"
        const val NOT_IN_LIST = "Not In List"

        fun launchDialogFragment(
            fragmentManager: FragmentManager,
            selectedworkItem: String?,
            onDialogClickListener: OnDialogClickListener
        ){
            val fragment = WorkCategoryDialogFragment()
            fragment.arguments = bundleOf(INTENT_EXTRA_SELECTED_ITEMS to selectedworkItem)
            fragment.onDialogClickListener = onDialogClickListener
            fragment.show(fragmentManager, TAG)
        }
    }
    override fun onWorkCategoryClick(workCategory: WorkCategory) {
        if (workCategory.isBoxChecked && checkedItemList.contains(workCategory.work).not())
            checkedItemList.add(workCategory.work!!)
        else if (workCategory.isBoxChecked.not() && checkedItemList.contains(workCategory.work))
            checkedItemList.remove(workCategory.work)
    }
}