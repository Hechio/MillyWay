package com.stevehechio.milkyway.data.repository

import io.reactivex.disposables.Disposable

interface BaseRepository {
    fun addDisposable(disposable: Disposable)

    fun clear()
}