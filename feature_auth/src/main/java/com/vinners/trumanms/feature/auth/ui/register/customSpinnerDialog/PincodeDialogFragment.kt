package com.vinners.trumanms.feature.auth.ui.register.customSpinnerDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.vinners.trumanms.core.base.BaseDialogFragment
import com.vinners.trumanms.core.extensions.setVisibilityGone
import com.vinners.trumanms.core.extensions.setVisibilityVisible
import com.vinners.trumanms.core.taskState.Lce
import com.vinners.trumanms.data.models.stateAndCity.Pincode
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.FragmentPincodeDialogBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import java.util.*
import javax.inject.Inject

class PincodeDialogFragment :
    BaseDialogFragment<FragmentPincodeDialogBinding, StateCityViewModel>(R.layout.fragment_pincode_dialog),
    PincodeOnClickListener {
    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory

    lateinit var onDialogClickListener: OnDialogClickListener

    private var cityId: String? = null

    override val viewModel: StateCityViewModel by viewModels {
        viewModelFactory
    }

    private val pincodeRecyclerAdapter: PincodeRecyclerAdapter by lazy {
        PincodeRecyclerAdapter().apply {
            updateList(Collections.emptyList())
            setPincodeOnClickListener(this@PincodeDialogFragment)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            cityId = it.getString(INTENT_EXTRA_CITY_ID) ?: return@let
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
        viewBinding.pincodeRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.pincodeRecyclerView.adapter = pincodeRecyclerAdapter
        viewBinding.pincodeSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                pincodeRecyclerAdapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onInitViewModel() {
        viewModel.cityAndPincode.observe(this, androidx.lifecycle.Observer {
            when (it) {
                Lce.Loading -> {
                    viewBinding.progerssBar.setVisibilityVisible()
                }
                is Lce.Content -> {
                    viewBinding.progerssBar.setVisibilityGone()
                    val pincodeList = it.content.pincodeList.sortedBy {
                        it.pincode
                    }
                    val pincodeCustomList = mutableListOf<Pincode>()
                    pincodeCustomList.add(0, Pincode(null, "Not In List"))
                    pincodeCustomList.addAll(pincodeList)
                    pincodeRecyclerAdapter.updateList(pincodeCustomList)
                }
                is Lce.Error -> {
                    viewBinding.progerssBar.setVisibilityGone()
                }
            }
        })
        viewModel.getCityAndPincode(cityId)
    }

    override fun onPincodeClick(pincode: Pincode) {
        onDialogClickListener.onPincodeClick(pincode)
        dismiss()
    }

    companion object {
        const val TAG = "pincodeDialogFragment"
        const val INTENT_EXTRA_CITY_ID = "cityId"
        const val PINCODE = "Pincode*"
        const val NOT_IN_LIST = "Not In List"

        fun launchDialogFragment(
            fragmentManager: FragmentManager,
            cityId: String?,
            onDialogClickListener: OnDialogClickListener
        ) {
            val fragment = PincodeDialogFragment()
            fragment.arguments = bundleOf(INTENT_EXTRA_CITY_ID to cityId)
            fragment.onDialogClickListener = onDialogClickListener
            fragment.show(fragmentManager, TAG)
        }
    }
}