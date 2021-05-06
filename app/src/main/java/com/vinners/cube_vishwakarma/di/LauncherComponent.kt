package com.vinners.cube_vishwakarma.di


import com.vinners.cube_vishwakarma.core.di.CoreComponent
import com.vinners.cube_vishwakarma.core.di.scopes.FeatureScope
import com.vinners.cube_vishwakarma.notification.NotificationService
import com.vinners.cube_vishwakarma.ui.languageSelection.SelectLanguageFragment
import com.vinners.cube_vishwakarma.ui.LauncherActivity
import com.vinners.cube_vishwakarma.ui.MainActivity
import com.vinners.cube_vishwakarma.ui.appVersionDiscontinued.VersionBlockActivity
import com.vinners.cube_vishwakarma.ui.complaints.ComplaintsActivity
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.MyComplaintActivity
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.*
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.myComplaintDetails.MyComplaintDetailsActivity
import com.vinners.cube_vishwakarma.ui.splash.SplashActivity
import com.vinners.cube_vishwakarma.ui.splash.SplashFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [LauncherModule::class],
    dependencies = [CoreComponent::class]
)
interface LauncherComponent {

    fun inject(launcherActivity: LauncherActivity)

    fun inject(notificationService: NotificationService)

    fun inject(splashActivity: SplashActivity)

    fun inject(splashFragment: SplashFragment)

    fun inject(languageFragment: SelectLanguageFragment)

    fun inject(versionBlockActivity: VersionBlockActivity)

    fun inject(mainActivity: MainActivity)

    fun inject(complaintsActivity: ComplaintsActivity)

    fun inject(myComplaintActivity: MyComplaintActivity)

    fun inject(allFragment: AllFragment)

    fun inject(dueFragment: DueFragment)

    fun inject(cancelledFragment: CancelledFragment)

    fun inject(doneFragment: DoneFragment)

    fun inject(holdFragment: HoldFragment)

    fun inject(workingFragment: WorkingFragment)

    fun inject(billedFragment: BilledFragment)

    fun inject(estimatedFragment: EstimatedFragment)

    fun inject(paymentFragment: PaymentFragment)

    fun inject(draftFragment: DraftFragment)

    fun inject(myComplaintDetailsActivity: MyComplaintDetailsActivity)
}