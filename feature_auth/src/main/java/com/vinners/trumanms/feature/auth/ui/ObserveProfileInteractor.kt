package com.vinners.trumanms.feature.auth.ui

import com.vinners.trumanms.core.executors.PostExecutionThread
import com.vinners.trumanms.core.executors.ThreadExecutor
import com.vinners.trumanms.core.observerUseCase.ObservableUseCase
import com.vinners.trumanms.data.models.profile.Profile
import com.vinners.trumanms.data.repository.AuthRepository
import io.reactivex.Observable
import javax.inject.Inject

class ObserveProfileInteractor @Inject constructor(private val authRepository: AuthRepository
                                                   , private val threadExecutor: ThreadExecutor
                                                   , private val postExecutionThread: PostExecutionThread
) : ObservableUseCase<Any?, Profile>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Any?): Observable<Profile> {
        return authRepository.observeProfile()
    }
}
