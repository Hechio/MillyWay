package com.stevehechio.milkyway.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.stevehechio.milkyway.db.AppDatabase
import com.stevehechio.milkyway.local.entities.MilkyWayEntity
import com.stevehechio.milkyway.local.entities.RemoteKeys
import com.stevehechio.milkyway.data.remote.api.MilkyWayApiService
import retrofit2.HttpException
import java.io.IOException

private const val MILKY_WAY_STARTING_PAGE_INDEX = 1

@ExperimentalPagingApi
class MilkyWayRemoteMediator(
    private val service: MilkyWayApiService,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, MilkyWayEntity>(){

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MilkyWayEntity>
    ): MediatorResult {
        val page = when(loadType){
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: MILKY_WAY_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys !=null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val apiResponse = service.fetchMilkyWayImages()
            val milkyWayImages = apiResponse.result.items
            val endOfPaginationReached = milkyWayImages.isEmpty()
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH){
                    appDatabase.remoteKeysDao().clearRemoteKeys()
                    appDatabase.milkyWayDao().deleteAll()
                }
                val prevKey = if (page == MILKY_WAY_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = milkyWayImages.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.remoteKeysDao().insertAll(keys)
                appDatabase.milkyWayDao().insertAll(milkyWayImages)
            }
            return MediatorResult.Success(endOfPaginationReached)
        }catch (e: IOException){
            return MediatorResult.Error(e)
        } catch (e: HttpException){
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MilkyWayEntity>): RemoteKeys?{
        return state.pages.lastOrNull{it.data.isNotEmpty()}?.data?.lastOrNull()?.let {
            appDatabase.remoteKeysDao().getRemoteKeys(it.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MilkyWayEntity>): RemoteKeys?{
        return state.pages.firstOrNull {it.data.isNotEmpty()}?.data?.firstOrNull()?.let {
            appDatabase.remoteKeysDao().getRemoteKeys(it.id)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MilkyWayEntity>) : RemoteKeys?{
        return state.anchorPosition?.let { pos ->
            state.closestItemToPosition(pos)?.id?.let { id ->
                appDatabase.remoteKeysDao().getRemoteKeys(id)
            }
        }
    }
}