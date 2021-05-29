package com.vinners.cube_vishwakarma.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vinners.cube_vishwakarma.core.di.modules.ViewModelKey
import com.vinners.cube_vishwakarma.ui.MainActivityViewModel
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.ComplaintRequestViewModel
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.MyComplaintViewModel
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.viewModel.AllComplaintFragmentViewModel
import com.vinners.cube_vishwakarma.ui.documents.DocumentsViewModel
import com.vinners.cube_vishwakarma.ui.languageSelection.LanguageViewModel
import com.vinners.cube_vishwakarma.ui.outlets.OutletsViewModel
import com.vinners.cube_vishwakarma.ui.profile.ProfileActivityViewModel
import com.vinners.cube_vishwakarma.ui.splash.SplashViewModel
import com.vinners.cube_vishwakarma.ui.tutorials.TutorialsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LauncherModule {

    @Binds
    abstract fun bindViewModelFactory(factoryAuth: LauncherViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindLauncherViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LanguageViewModel::class)
    abstract fun bindLanguageViewModel(viewModel: LanguageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyComplaintViewModel::class)
    abstract fun bindMyComplaintViewModel(viewModel: MyComplaintViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AllComplaintFragmentViewModel::class)
    abstract fun bindAllComplaintFragmentViewModel(viewModel: AllComplaintFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OutletsViewModel::class)
    abstract fun bindOutletsViewModel(viewModel: OutletsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComplaintRequestViewModel::class)
    abstract fun bindComplaintRequestViewModel(viewModel: ComplaintRequestViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileActivityViewModel::class)
    abstract fun bindProfileActivityViewModel(viewModel: ProfileActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TutorialsViewModel::class)
    abstract fun bindTutorialsViewModel(viewModel: TutorialsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DocumentsViewModel::class)
    abstract fun bindDocumentsViewModel(viewModel: DocumentsViewModel): ViewModel

}