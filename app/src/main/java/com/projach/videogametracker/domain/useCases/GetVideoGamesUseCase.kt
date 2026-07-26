package com.projach.videogametracker.domain.useCases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.projach.videogametracker.data.paging.VideoGamesPaging
import com.projach.videogametracker.domain.VideoGamesRepository
import com.projach.videogametracker.domain.models.VideoGameModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVideoGamesUseCase @Inject constructor(
    private val repository: VideoGamesRepository
) {
    fun invoke(): Flow<PagingData<VideoGameModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { VideoGamesPaging(repository) }
        ).flow
    }
}