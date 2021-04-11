package com.vinners.trumanms.feature_job.di

import com.vinners.trumanms.core.di.CoreComponent
import com.vinners.trumanms.core.di.scopes.FeatureScope
import com.vinners.trumanms.feature_job.ui.jobList.jobList.JobListFragment
import com.vinners.trumanms.feature_job.ui.jobList.jobDetails.JobDetailsActivity
import com.vinners.trumanms.feature_job.ui.jobList.jobList.StateFilterDialogFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [
        JobsModule::class
    ],
    dependencies = [CoreComponent::class]
)
interface JobsComponent {

    fun inject(fragment: JobListFragment)

    fun inject(jobDetailsActivity: JobDetailsActivity)

    fun inject(stateFilterDialogFragment: StateFilterDialogFragment)

}