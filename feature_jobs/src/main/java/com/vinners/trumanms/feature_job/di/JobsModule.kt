package com.vinners.trumanms.feature_job.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vinners.trumanms.core.di.modules.ViewModelKey
import com.vinners.trumanms.feature_job.ui.jobList.jobList.JobsViewModel
import com.vinners.trumanms.feature_job.ui.jobList.jobDetails.JobDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class JobsModule {

    @Binds
    abstract fun bindViewModelFactory(factoryAuth: JobsViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(JobsViewModel::class)
    abstract fun bindJobsViewModel(viewModel: JobsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobDetailsViewModel::class)
    abstract fun bindJobDetailsViewModel(viewModel: JobDetailsViewModel): ViewModel


}