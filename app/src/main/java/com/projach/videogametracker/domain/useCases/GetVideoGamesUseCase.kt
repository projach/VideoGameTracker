package com.projach.videogametracker.domain.useCases

import com.projach.videogametracker.domain.DataResult
import com.projach.videogametracker.domain.VideoGamesRepository
import com.projach.videogametracker.domain.models.VideoGameModel
import javax.inject.Inject

class GetVideoGamesUseCase @Inject constructor(
    private val repository: VideoGamesRepository
) {
    suspend fun invoke(): DataResult<List<VideoGameModel>>{
        return repository.fetchVideoGames()
    }
}