package com.example.feature_profile.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.feature_profile.ui.ProfileViewModel
import com.example.feature_profile.ui.bankDetails.BankDetailsViewModel
import com.example.feature_profile.ui.certificate.CertificateViewModel
import com.example.feature_profile.ui.documents.DocumentsViewModel
import com.example.feature_profile.ui.jobHistory.JobHistoryViewModel
import com.example.feature_profile.ui.kyc.KycViewModel
import com.vinners.trumanms.core.di.modules.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileModule {

    @Binds
    abstract fun bindViewModelFactory(factoryAuth: ProfileViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BankDetailsViewModel::class)
    abstract fun bindBankDetailsViewModel(viewModel: BankDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DocumentsViewModel::class)
    abstract fun bindDocumentsViewModel(viewModel: DocumentsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(JobHistoryViewModel::class)
    abstract fun bindJobHistoryViewModel(viewModel: JobHistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CertificateViewModel::class)
    abstract fun bindCertificateViewModel(viewModel: CertificateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(KycViewModel::class)
    abstract fun bindKycViewModel(viewModel: KycViewModel): ViewModel
}