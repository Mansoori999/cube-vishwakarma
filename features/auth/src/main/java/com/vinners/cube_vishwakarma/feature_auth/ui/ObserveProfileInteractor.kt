package com.vinners.cube_vishwakarma.feature_auth.ui

import com.vinners.cube_vishwakarma.core.executors.PostExecutionThread
import com.vinners.cube_vishwakarma.core.executors.ThreadExecutor
import com.vinners.cube_vishwakarma.core.observerUseCase.ObservableUseCase
import com.vinners.cube_vishwakarma.data.models.profile.Profile
import com.vinners.cube_vishwakarma.data.repository.AuthRepository

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
