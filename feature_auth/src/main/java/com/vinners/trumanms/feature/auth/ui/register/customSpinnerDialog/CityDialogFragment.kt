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
import com.vinners.trumanms.data.models.stateAndCity.City
import com.vinners.trumanms.feature.auth.R
import com.vinners.trumanms.feature.auth.databinding.FragmentCityDialogBinding
import com.vinners.trumanms.feature.auth.di.AuthViewModelFactory
import com.vinners.trumanms.feature.auth.di.DaggerAuthComponent
import java.util.*
import javax.inject.Inject

class CityDialogFragment: BaseDialogFragment<FragmentCityDialogBinding,StateCityViewModel>(R.layout.fragment_city_dialog),CityOnClickListener {

    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory

    lateinit var onDialogClickListener: OnDialogClickListener

    private  var stateId: String? = null

    override val viewModel: StateCityViewModel by viewModels {
        viewModelFactory
    }

    private val cityRecyclerAdapter: CityRecyclerAdapter by lazy {
        CityRecyclerAdapter().apply {
            updateList(Collections.emptyList())
            setCityOnClickListener(this@CityDialogFragment)
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
            stateId = it.getString(INTENT_EXTRA_STATE_ID) ?: return@let
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
      viewBinding.cityRecyclerView.itemAnimator = DefaultItemAnimator()
        viewBinding.cityRecyclerView.adapter = cityRecyclerAdapter
        viewBinding.citySearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               cityRecyclerAdapter.filter.filter(newText)
                return false
            }

        })
    }

    override fun onInitViewModel() {
        viewModel.cityAndPincode.observe(this, androidx.lifecycle.Observer {
            when(it){
                Lce.Loading -> {
                    viewBinding.progerssBar.setVisibilityVisible()
                }
                is Lce.Content -> {
                    viewBinding.progerssBar.setVisibilityGone()
                    val cityList = it.content.cityList.sortedBy {
                        it.cityName
                    }
                   /* val cityCustomList = mutableListOf<City>()
                    cityCustomList.add(0,City("Not In List",null,districtiId = null))
                    cityCustomList.addAll(cityList)*/
                    cityRecyclerAdapter.updateList(cityList)
                }
                is Lce.Error -> {
                    viewBinding.progerssBar.setVisibilityGone()
                }
            }
        })
        viewModel.getCityAndPincode(stateId)
    }

    override fun onCityClick(city: City) {
        onDialogClickListener.onCityClick(city)
        dismiss()
    }



    companion object{
        const val TAG = "cityDialogFragment"
        const val INTENT_EXTRA_STATE_ID = "stateId"
        const val CITY = "District*"
        const val NOT_IN_LIST = "Not In List"

        fun launchDialogFragment(
            fragmentManager: FragmentManager,
            stateId: String?,
           onDialogClickListener: OnDialogClickListener
        ){
            val fragment = CityDialogFragment()
            fragment.arguments = bundleOf(INTENT_EXTRA_STATE_ID to stateId)
            fragment.onDialogClickListener = onDialogClickListener
            fragment.show(fragmentManager, TAG)
        }
    }
}