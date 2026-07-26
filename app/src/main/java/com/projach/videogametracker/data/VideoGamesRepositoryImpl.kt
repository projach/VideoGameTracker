package com.projach.videogametracker.data

import com.projach.videogametracker.data.source.remote.IgdbApi
import com.projach.videogametracker.data.source.remote.apiCallAsDataResult
import com.projach.videogametracker.domain.DataResult
import com.projach.videogametracker.domain.PAGE_LIMIT
import com.projach.videogametracker.domain.VideoGamesRepository
import com.projach.videogametracker.domain.buildIgdbBodyTopRated
import com.projach.videogametracker.domain.mapSuccess
import com.projach.videogametracker.domain.models.VideoGameModel
import com.projach.videogametracker.domain.onSuccess
import com.projach.videogametracker.utils.Logger
import java.util.Collections.emptyMap
import javax.inject.Inject

class VideoGamesRepositoryImpl @Inject constructor(
    private val igdbApi: IgdbApi
) : VideoGamesRepository {
    override suspend fun fetchVideoGames(
        page: Int
    ): DataResult<List<VideoGameModel>> {
        val videoGamesMap: MutableMap<Int, VideoGameModel> = mutableMapOf()

        return apiCallAsDataResult {
            igdbApi.getVideoGames(
                buildIgdbBodyTopRated(
                    limit = PAGE_LIMIT,
                    offset = PAGE_LIMIT * page,
                )
            )
        }.onSuccess {
            videoGamesMap.putAll(it.let { it?.map { it.toModel() }?.associateBy { it.id } ?: emptyMap() })
        }.mapSuccess {
            videoGamesMap.values.toList()
        }
    }
}