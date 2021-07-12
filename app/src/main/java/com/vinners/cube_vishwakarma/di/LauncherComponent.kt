package com.vinners.cube_vishwakarma.di


import android.view.View
import com.vinners.cube_vishwakarma.core.di.CoreComponent
import com.vinners.cube_vishwakarma.core.di.scopes.FeatureScope
import com.vinners.cube_vishwakarma.notification.NotificationService
import com.vinners.cube_vishwakarma.ui.languageSelection.SelectLanguageFragment
import com.vinners.cube_vishwakarma.ui.LauncherActivity
import com.vinners.cube_vishwakarma.ui.MainActivity
import com.vinners.cube_vishwakarma.ui.appVersionDiscontinued.VersionBlockActivity
import com.vinners.cube_vishwakarma.ui.attendance.AttendanceActivity

import com.vinners.cube_vishwakarma.ui.complaints.ComplaintsActivity
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequest.ComplaintRequestActivity
import com.vinners.cube_vishwakarma.ui.complaints.complaintRequestView.ComplaintRequestViewActivity
import com.vinners.cube_vishwakarma.ui.complaints.dashboardFragment.MonthlyFragment
import com.vinners.cube_vishwakarma.ui.complaints.dashboardFragment.ROWiseFragment
import com.vinners.cube_vishwakarma.ui.complaints.dashboardFragment.SummeryFragment
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.MyComplaintActivity
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.complainFragment.*
import com.vinners.cube_vishwakarma.ui.complaints.myComplaint.myComplaintDetails.MyComplaintDetailsActivity
import com.vinners.cube_vishwakarma.ui.documents.DocumentDetailsActivity
import com.vinners.cube_vishwakarma.ui.documents.DocumentsActivity
import com.vinners.cube_vishwakarma.ui.expense.ExpenseActivity
import com.vinners.cube_vishwakarma.ui.outlets.*
import com.vinners.cube_vishwakarma.ui.profile.BankDetailsAndOtherDetailsActivity
import com.vinners.cube_vishwakarma.ui.profile.ProfileActivity
import com.vinners.cube_vishwakarma.ui.profile.ProfileDetailsActivity
import com.vinners.cube_vishwakarma.ui.splash.SplashActivity
import com.vinners.cube_vishwakarma.ui.splash.SplashFragment
import com.vinners.cube_vishwakarma.ui.tutorials.TutorialsActivity
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

    fun inject(attendanceActivity: AttendanceActivity)

    fun inject(documentsActivity: DocumentsActivity)

    fun inject(expenseActivity: ExpenseActivity)

    fun inject(outletsActivity: OutletsActivity)

    fun inject(tutorialsActivity: TutorialsActivity)

    fun inject (complaintRequestActivity: ComplaintRequestActivity)

    fun inject (outletDetalisActivity : OutletDetalisActivity)

    fun inject(complaintRequestViewActivity : ComplaintRequestViewActivity)

    fun inject(profileActivity: ProfileActivity)

    fun inject(editOutletActivity: EditOutletActivity)

    fun inject(outletComplaintsActivity: OutletComplaintsActivity)

    fun inject(profileDetailsActivity: ProfileDetailsActivity)

    fun inject(bankDetailsAndOtheretailsActivity: BankDetailsAndOtherDetailsActivity)

    fun inject(documentDetailsActivity: DocumentDetailsActivity)

    fun inject(editGpsLocationActivity: EditGpsLocationActivity)

    fun inject(summeryFragment: SummeryFragment)

    fun inject(monthlyFragment: MonthlyFragment)

    fun inject(roWiseFragment: ROWiseFragment)



}