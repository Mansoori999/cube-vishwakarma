package com.example.notification.di

import com.example.notification.ui.NotificationActivity
import com.vinners.trumanms.core.di.CoreComponent
import com.vinners.trumanms.core.di.scopes.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    modules = [
        NotificationModule::class
    ],
    dependencies = [CoreComponent::class]
)
interface NotificationComponent {
    fun inject(notificationActivity: NotificationActivity)
}