package com.vinners.cube_vishwakarma.ui.complaints.dashboardFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.vinners.cube_vishwakarma.R
import com.vinners.cube_vishwakarma.core.base.BaseFragment
import com.vinners.cube_vishwakarma.data.sessionManagement.UserSessionManager
import com.vinners.cube_vishwakarma.databinding.FragmentSummeryBinding
import com.vinners.cube_vishwakarma.di.DaggerLauncherComponent
import com.vinners.cube_vishwakarma.di.LauncherViewModelFactory
import com.vinners.cube_vishwakarma.ui.MainActivityViewModel
import javax.inject.Inject


class SummeryFragment : BaseFragment<FragmentSummeryBinding, MainActivityViewModel>(R.layout.fragment_summery) {
    companion object {
        fun newInstance() = SummeryFragment()

    }

    @Inject
    lateinit var viewModelFactory: LauncherViewModelFactory

    @Inject
    lateinit var userSessionManager: UserSessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_summery, container, false)
    }

    override val viewModel: MainActivityViewModel by viewModels { viewModelFactory }

    override fun onInitDependencyInjection() {
        DaggerLauncherComponent
            .builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {

    }

    override fun onInitViewModel() {

    }


}