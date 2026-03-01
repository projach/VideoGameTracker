package com.projach.videogametracker.domain

import com.projach.videogametracker.domain.models.VideoGameModel

interface VideoGamesRepository {
    suspend fun fetchVideoGames(): DataResult<List<VideoGameModel>>
}