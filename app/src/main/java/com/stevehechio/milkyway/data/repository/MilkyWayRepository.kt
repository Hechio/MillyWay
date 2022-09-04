package com.stevehechio.milkyway.data.repository


import android.content.Context
import android.util.Log
import com.stevehechio.milkyway.data.Resource
import com.stevehechio.milkyway.data.local.entities.MilkyWayEntity
import com.stevehechio.milkyway.data.remote.api.MilkyWayApiService
import com.stevehechio.milkyway.db.AppDatabase
import com.stevehechio.milkyway.utils.NetworkUtil
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class MilkyWayRepository @Inject  constructor(
    private val application: Context,
    private val service: MilkyWayApiService,
    private val appDatabase: AppDatabase
): BaseRepository{
    private val compositeDisposable = CompositeDisposable()

    fun getMilkyWayResults(): Observable<Resource<List<MilkyWayEntity>>> {
        return if (NetworkUtil.isNetworkAvailable(application)){
            getRemoteMilkyImages()
        }else {
            getLocalMilkyImages()
        }
    }

    private fun getLocalMilkyImages():Observable<Resource<List<MilkyWayEntity>>>{
        return Observable.create{ emitter ->
            emitter.onNext(Resource.Loading())
            val disposable = appDatabase.milkyWayDao().getMilkyWayImages()
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    emitter.onNext( Resource.Success(it))
                    Log.d("milky res", "Success Execution! $it")
                },{
                    emitter.onNext(Resource.Failure(it.localizedMessage))
                    Log.e("milky res", "Error Execution! $it")
                })
            addDisposable(disposable)
        }
    }

    private fun getRemoteMilkyImages():Observable<Resource<List<MilkyWayEntity>>>{
        return Observable.create{ emitter ->
            emitter.onNext(Resource.Loading())
            val disposable = service.fetchMilkyWayImages()
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    Completable.fromRunnable {
                        appDatabase.milkyWayDao().deleteAll()
                        appDatabase.milkyWayDao().insertAll(it.result.items)
                    }.subscribeOn(io()).subscribe()
                }
                .subscribe({
                    emitter.onNext( Resource.Success(it.result.items))
                    Log.d("milky res", "Success Execution! $it")
                },{
                    emitter.onNext(Resource.Failure(it.localizedMessage))
                    Log.e("milky res", "Error Execution! $it")
                })
            addDisposable(disposable)
        }
    }


    override fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun clear() {
        compositeDisposable.clear()
    }
}