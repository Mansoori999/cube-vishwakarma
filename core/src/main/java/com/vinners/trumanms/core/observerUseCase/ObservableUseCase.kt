package com.vinners.trumanms.core.observerUseCase

import com.vinners.trumanms.core.executors.PostExecutionThread
import com.vinners.trumanms.core.executors.ThreadExecutor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

abstract class ObservableUseCase<Params,ResponseType> constructor(
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
) : UseCase {

    private val disposables = CompositeDisposable()

    /**
     * Builds an [Observable] which will be used when executing the current [ObservableUseCase].
     */
     abstract fun buildUseCaseObservable(params: Params): Observable<ResponseType>

    /**
     * Executes the current use case.
     *
     * @param observer [DisposableObserver] which will be listening to the observable build
     * by [.buildUseCaseObservable] ()} method.
     * @param params Parameters (Optional) used to build/syncPaymentsToServer this use case.
     */
    fun execute(observer: DisposableObserver<ResponseType>, params: Params) {
        val observable = this.buildUseCaseObservable(params)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.scheduler)
        addDisposable(observable.subscribeWith(observer))
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    fun dispose() {
        if (disposables.isDisposed.not()) {
            disposables.dispose()
        }
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}