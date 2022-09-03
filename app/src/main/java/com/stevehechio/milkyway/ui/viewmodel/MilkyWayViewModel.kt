package com.stevehechio.milkyway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.stevehechio.milkyway.local.entities.MilkyWayEntity
import com.stevehechio.milkyway.data.repository.MilkyWayRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MilkyWayViewModel
@Inject constructor(private val repository: MilkyWayRepository): ViewModel(){
    @ExperimentalPagingApi
    fun fetchImages(): Flow<PagingData<MilkyWayEntity>>{
        return repository.getMilkyWayResults()
    }
}