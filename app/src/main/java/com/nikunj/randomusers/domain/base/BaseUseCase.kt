package com.nikunj.randomusers.domain.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
import timber.log.Timber

abstract class BaseUseCase<T> : Disposable {

    protected var disposables = CompositeDisposable()

    protected fun disposableObserver(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = {}
    ): DisposableObserver<T> {

        return object : DisposableObserver<T>() {
            override fun onComplete() {
                Timber.i("onComplete...")
            }

            override fun onNext(value: T) {
                Timber.i("onNext...")
                onNext.invoke(value)
            }

            override fun onError(e: Throwable) {
                Timber.e( "onError... ${e.localizedMessage}")
                onError.invoke(e)
            }
        }
    }

    protected fun disposableSingleObserver(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit = {}
    ): DisposableSingleObserver<T> {

        return object : DisposableSingleObserver<T>() {
            override fun onSuccess(value: T) {
                Timber.i("onNext...")
                onNext.invoke(value)
            }

            override fun onError(e: Throwable) {
                Timber.e( "onError... ${e.localizedMessage}")
                onError.invoke(e)
            }
        }
    }

    protected fun disposableCompletableObserver(
        onComplete: () -> Unit,
        onError: (Throwable) -> Unit = {}
    ): DisposableCompletableObserver {

        return object : DisposableCompletableObserver() {

            override fun onComplete() {
                Timber.i("onComplete...")
                onComplete.invoke()
            }

            override fun onError(e: Throwable) {
                Timber.e( "onError... ${e.localizedMessage}")
                onError.invoke(e)
            }
        }
    }

    override fun dispose() {
        disposables.clear()
    }

    override fun isDisposed(): Boolean {
        return disposables.isDisposed
    }
}