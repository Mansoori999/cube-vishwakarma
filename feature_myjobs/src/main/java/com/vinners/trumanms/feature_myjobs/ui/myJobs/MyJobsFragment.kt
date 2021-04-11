package com.vinners.trumanms.feature_myjobs.ui.myJobs

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vinners.trumanms.core.base.BaseFragment
import com.vinners.trumanms.feature_myjobs.R
import com.vinners.trumanms.feature_myjobs.databinding.FragmentMyJobsBinding
import com.vinners.trumanms.feature_myjobs.di.DaggerMyJobsComponent
import com.vinners.trumanms.feature_myjobs.di.MyJobsViewModelFactory
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.appliedJobs.AppliedJobsFragment
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.inHandJobs.InHandJobsFragment
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.myOffers.MyOffersFragment
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.savedJobs.SavedJobsFragment
import com.vinners.trumanms.feature_myjobs.views.MyJobsTabLayout
import javax.inject.Inject


class MyJobsFragment :
    BaseFragment<FragmentMyJobsBinding, MyJobsViewModel>(R.layout.fragment_my_jobs),MyOffersFragment.AcceptTaskListener {
    companion object{
        const val INTENT_EXTRA_NOTIFICATION_TYPE = "type"
    }
    @Inject
    lateinit var viewModelFactory: MyJobsViewModelFactory

    private var acceptedJobNotify: String? = null

    override val viewModel: MyJobsViewModel by viewModels { viewModelFactory }

   /* private val myOffersFragment: Fragment by lazy {
        MyOffersFragment()
    }

    private val inHandJobsFragment: Fragment by lazy {
        InHandJobsFragment()
    }

    private val appliedJobsFragment: Fragment by lazy {
        AppliedJobsFragment()
    }

    private val savedJobsFragment: Fragment by lazy {
        SavedJobsFragment()
    }*/

    override fun onInitDependencyInjection() {
        DaggerMyJobsComponent.builder()
            .coreComponent(getCoreComponent())
            .build()
            .inject(this)
    }

    override fun onInitDataBinding() {
        ///Loading the Fragments
        acceptedJobNotify = arguments?.getString(INTENT_EXTRA_NOTIFICATION_TYPE,null)
        if (acceptedJobNotify.equals("accept",true)) {
            viewBinding.myJobsTabLayout.setItemAsSelected(1)
            val inHandJobsFragment = InHandJobsFragment()
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.my_jobs_container_view, inHandJobsFragment, InHandJobsFragment.TAG)
            fragmentTransaction.disallowAddToBackStack()
            fragmentTransaction.commit()
        }else{
            val myOffersFragment = MyOffersFragment()
            myOffersFragment.setListener(this@MyJobsFragment)
            val fragmentTransaction = childFragmentManager.beginTransaction()
            fragmentTransaction.add(
                R.id.my_jobs_container_view,
                myOffersFragment,
                MyOffersFragment.TAG
            )
            fragmentTransaction.disallowAddToBackStack()
            fragmentTransaction.commit()
        }

        viewBinding.myJobsTabLayout.setOnTabClickListener { selectedPosition ->

            when (selectedPosition) {
                MyJobsTabLayout.POSITION_MY_OFFERS -> replaceWithMyOffersFragment()
                MyJobsTabLayout.POSITION_SAVED_JOBS -> replaceWithSavedJobFragment()
                MyJobsTabLayout.POSITION_IN_HAND -> replaceWithInHandFragment()
                MyJobsTabLayout.POSITION_APPLIED_JOBS -> replaceWithAppliedFragment()
            }
        }
    }

    private fun replaceWithMyOffersFragment() {
        val myOffersFragment = MyOffersFragment()
        myOffersFragment.setListener(this@MyJobsFragment)
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.my_jobs_container_view, myOffersFragment, MyOffersFragment.TAG)
        fragmentTransaction.disallowAddToBackStack()
        fragmentTransaction.commit()
    }

    private fun replaceWithSavedJobFragment() {
        val savedJobsFragment = SavedJobsFragment()
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.my_jobs_container_view, savedJobsFragment, SavedJobsFragment.TAG)
        fragmentTransaction.disallowAddToBackStack()
        fragmentTransaction.commit()
    }

    private fun replaceWithInHandFragment() {
        val inHandJobsFragment = InHandJobsFragment()
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.my_jobs_container_view, inHandJobsFragment, InHandJobsFragment.TAG)
        fragmentTransaction.disallowAddToBackStack()
        fragmentTransaction.commit()
    }

    private fun replaceWithAppliedFragment() {
        val appliedJobsFragment = AppliedJobsFragment()
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.my_jobs_container_view, appliedJobsFragment, AppliedJobsFragment.TAG)
        fragmentTransaction.disallowAddToBackStack()
        fragmentTransaction.commit()
    }

    override fun onInitViewModel() {
    }

    override fun onAccept() {
        viewBinding.myJobsTabLayout.setItemAsSelected(1)
        val inHandJobsFragment = InHandJobsFragment()
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.my_jobs_container_view, inHandJobsFragment, InHandJobsFragment.TAG)
        fragmentTransaction.disallowAddToBackStack()
        fragmentTransaction.commit()
    }
}