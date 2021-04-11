package com.vinners.trumanms.feature_help.di

import com.vinners.trumanms.core.di.CoreComponent
import com.vinners.trumanms.core.di.scopes.FeatureScope
import com.vinners.trumanms.feature_help.ui.FaqsFragment
import com.vinners.trumanms.feature_help.ui.HelpFragment
import com.vinners.trumanms.feature_help.ui.QueryFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [
        HelpModule::class
    ],
    dependencies = [CoreComponent::class]
)
interface HelpComponent {


    fun inject(helpFragment: HelpFragment)

    fun inject(faqsFragment: FaqsFragment)

    fun inject(queryFragment: QueryFragment)
}