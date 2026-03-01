package com.projach.videogametracker.data

import android.util.Log
import com.projach.videogametracker.data.source.remote.IgdbApi
import com.projach.videogametracker.data.source.remote.apiCallAsDataResult
import com.projach.videogametracker.domain.DataResult
import com.projach.videogametracker.domain.PAGE_LIMIT
import com.projach.videogametracker.domain.VideoGamesRepository
import com.projach.videogametracker.domain.buildIgdbBodyTopRated
import com.projach.videogametracker.domain.mapSuccess
import com.projach.videogametracker.domain.models.VideoGameModel
import com.projach.videogametracker.domain.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

class VideoGamesRepositoryImpl @Inject constructor(
    private val igdbApi: IgdbApi
): VideoGamesRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var page = 0
    private val mutex =  Mutex()
    private val videoGamesMap: LinkedHashMap<Int, VideoGameModel> = linkedMapOf()

    override suspend fun fetchVideoGames(): DataResult<List<VideoGameModel>> {
        return apiCallAsDataResult {
            igdbApi.getVideoGames(
                buildIgdbBodyTopRated(
                    limit = PAGE_LIMIT,
                    offset = PAGE_LIMIT * page,
                )
            )
        }.onSuccess {
//            scope.launch {
//                mutex.withLock {
                    videoGamesMap.putAll(
                        it?.let { it.map { it.toModel() }.associateBy { it.id } } ?: emptyMap()
                    )
//                }
//            }
            //add new page to get next items
            page++
        }.mapSuccess {
            Log.d("Map success", "List of games: ${videoGamesMap.values.toList().joinToString()}")
            videoGamesMap.values.toList()
        }
    }
}