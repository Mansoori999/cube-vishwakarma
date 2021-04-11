package com.example.feature_profile.di
import com.example.feature_profile.ui.LogOutActivity
import com.example.feature_profile.ui.LogoutFragment
import com.example.feature_profile.ui.ProfileActivity
import com.example.feature_profile.ui.bankDetails.BankDetailsActivity
import com.example.feature_profile.ui.certificate.CertificateActivity
import com.example.feature_profile.ui.documents.DocumentsActivity
import com.example.feature_profile.ui.documents.ViewDocumentsOnlineActivity
import com.example.feature_profile.ui.jobHistory.JobHistoryActivity
import com.example.feature_profile.ui.kyc.KycActivity
import com.example.feature_profile.ui.medical.MedicalActivity
import com.vinners.trumanms.core.di.CoreComponent
import com.vinners.trumanms.core.di.scopes.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    modules = [
        ProfileModule::class
    ],
    dependencies = [CoreComponent::class]
)
interface ProfileComponent {

    fun inject(profileActivity: ProfileActivity)

    fun inject(logoutFragment: LogoutFragment)

    fun inject(logOutActivity: LogOutActivity)

    fun inject(bankDetailsActivity: BankDetailsActivity)

    fun inject(documentsActivity: DocumentsActivity)

    fun inject(medicalActivity: MedicalActivity)

    fun inject(jobHistoryActivity: JobHistoryActivity)

    fun inject(certificateActivity: CertificateActivity)

    fun inject(kycActivity: KycActivity)

    fun inject(viewDocumentsOnlineActivity: ViewDocumentsOnlineActivity)

}

