package com.vinners.trumanms.feature_myjobs.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vinners.trumanms.core.di.modules.ViewModelKey
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.appliedJobs.AppliedJobsViewModel
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.inHandJobs.InHandJobsViewModel
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.myOffers.MyOffersViewModel
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.savedJobs.SavedJobsViewModel
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.withdraw.WithdrawApplicationViewModel
import com.vinners.trumanms.feature_myjobs.ui.myJobs.MyJobsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MyJobsModule {

    @Binds
    abstract fun bindViewModelFactory(factoryAuth: MyJobsViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MyJobsViewModel::class)
    abstract fun bindMyJobsViewModel(viewModel: MyJobsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AppliedJobsViewModel::class)
    abstract fun bindAppliedJobsViewModel(viewModel: AppliedJobsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InHandJobsViewModel::class)
    abstract fun bindInHandJobsViewModel(viewModel: InHandJobsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyOffersViewModel::class)
    abstract fun bindMyOffersViewModel(viewModel: MyOffersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavedJobsViewModel::class)
    abstract fun bindSavedJobsViewModel(viewModel: SavedJobsViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(WithdrawApplicationViewModel::class)
    abstract fun bindWithdrawApplicationViewModel(viewModel: WithdrawApplicationViewModel): ViewModel
}