package com.vinners.cube_vishwakarma.core.extensions

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.himanshu.trumanms.core.R
import timber.log.Timber

fun NavController.navigateSafely(
    @NonNull actionId: Int,
    args: Bundle? = null,
    popBackStack: Boolean = false
) {
    try {
        if (popBackStack)
            this.popBackStack()

        this.navigate(actionId, args)
    } catch (e: Exception) {
        Timber.e(e, "NavController : navigateSafely , Error While Navigating")
    }
}

fun getNavOptions() = navOptions {
    this.anim {
        this.enter = R.anim.anim_enter_from_right
        this.exit = R.anim.anim_exit_to_left
        this.popEnter = R.anim.anim_enter_from_left
        this.popExit = R.anim.anim_exit_to_right
    }
}