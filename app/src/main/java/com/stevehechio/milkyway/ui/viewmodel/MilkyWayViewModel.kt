package com.stevehechio.milkyway.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.stevehechio.milkyway.data.Resource
import com.stevehechio.milkyway.data.local.entities.MilkyWayEntity
import com.stevehechio.milkyway.data.repository.MilkyWayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MilkyWayViewModel
@Inject constructor(private val repository: MilkyWayRepository): ViewModel(){
    private val compositeDisposable = CompositeDisposable()
    private val milkyLiveData: MutableLiveData<Resource<List<MilkyWayEntity>>> =
        MutableLiveData<Resource<List<MilkyWayEntity>>>()

    fun getMilkyLiveData(): MutableLiveData<Resource<List<MilkyWayEntity>>> = milkyLiveData

    fun fetchImages(){
       addToDisposable(
           repository.getMilkyWayResults()
               .subscribeOn(Schedulers.io())
               .doOnSubscribe{milkyLiveData.value = Resource.Loading()}
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe({
                   milkyLiveData.value = it
               },{
                   milkyLiveData.value = Resource.Failure(it.localizedMessage)
               })
       )
    }

    private fun addToDisposable(disposable: Disposable?) {
        compositeDisposable.remove(disposable!!)
        compositeDisposable.add(disposable)
    }

    private fun onStop() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        super.onCleared()
        onStop()
    }
}