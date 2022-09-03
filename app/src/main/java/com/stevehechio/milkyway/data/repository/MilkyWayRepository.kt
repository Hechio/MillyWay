package com.stevehechio.milkyway.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.stevehechio.milkyway.db.AppDatabase
import com.stevehechio.milkyway.local.entities.MilkyWayEntity
import com.stevehechio.milkyway.data.remote.api.MilkyWayApiService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MilkyWayRepository @Inject  constructor(
    private val service: MilkyWayApiService,
    private val appDatabase: AppDatabase
): BaseRepository{
    private val compositeDisposable = CompositeDisposable()

    @ExperimentalPagingApi
    fun getMilkyWayResults(): Flow<PagingData<MilkyWayEntity>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = MilkyWayRemoteMediator(service,appDatabase)
        ){appDatabase.milkyWayDao().getAllMilkyWayImages()}.flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 25
    }
    override fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun clear() {
        compositeDisposable.clear()
    }
}