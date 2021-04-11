package com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog

import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.vinners.trumanms.core.base.BaseDialogFragment
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.stateAndCity.State
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.FragmentStateDialogBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import java.util.*
import javax.inject.Inject

class StateDialogFragment: BaseDialogFragment<FragmentStateDialogBinding,StateCityViewModel>(R.layout.fragment_state_dialog),StateOnClickListener{
    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory

    lateinit var onDialogClickListener: OnDialogClickListener

    override val viewModel: StateCityViewModel by viewModels {
        viewModelFactory
    }


    private val stateRecyclerAdapter: StateRecyclerAdapter by lazy {
        StateRecyclerAdapter().apply {
            updateList(Collections.emptyList())
            setStateOnClickListener(this@StateDialogFragment)
        }
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
        DaggerAuthComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        viewBinding.stateRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.stateRecyclerView.adapter = stateRecyclerAdapter
        viewBinding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                stateRecyclerAdapter.filter.filter(newText)
                return false
            }

        })
    }

    override fun onInitViewModel() {
            viewModel.state.observe(this, Observer {
                when(it){
                    Lce.Loading -> {

                    }
                    is Lce.Content -> {
                        stateRecyclerAdapter.updateList(it.content)
                    }
                    is Lce.Error -> {

                    }
                }
            })
        viewModel.getState()
    }

    override fun onStateClick(state: State) {
        onDialogClickListener.onStateClick(state)
        dismiss()
    }


    companion object{
        const val TAG = "stateDialogFragment"
        const val STATE = "State"

        fun launchDialogFragment(
            fragmentManager: FragmentManager,
            onDialogClickListener: OnDialogClickListener
        ){
            val fragment = StateDialogFragment()
            fragment.onDialogClickListener = onDialogClickListener
            fragment.show(fragmentManager, TAG)
        }
    }
}