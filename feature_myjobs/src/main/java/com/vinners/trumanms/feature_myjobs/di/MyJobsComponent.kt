package com.vinners.trumanms.feature_myjobs.di

import com.vinners.trumanms.core.di.CoreComponent
import com.vinners.trumanms.core.di.scopes.FeatureScope
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.appliedJobs.AppliedJobsFragment
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.inHandJobs.InHandJobsFragment
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.inHandJobs.notes.NotesActivity
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.myOffers.MyOffersFragment
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.savedJobs.SavedJobsFragment
import com.vinners.trumanms.feature_myjobs.ui.innerFragments.withdraw.WithdrawApplicationDialogFragment
import com.vinners.trumanms.feature_myjobs.ui.myJobs.MyJobsFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [
        MyJobsModule::class
    ],
    dependencies = [CoreComponent::class]
)
interface MyJobsComponent {

    fun inject(fragment: MyJobsFragment)

    fun inject(fragment: AppliedJobsFragment)

    fun inject(fragment: InHandJobsFragment)

    fun inject(fragment: MyOffersFragment)

    fun inject(fragment: SavedJobsFragment)

    fun inject(notesActivity: NotesActivity)

    fun inject(fragment: WithdrawApplicationDialogFragment)
}