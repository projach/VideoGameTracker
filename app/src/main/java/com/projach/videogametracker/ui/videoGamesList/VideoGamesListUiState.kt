package com.projach.videogametracker.ui.videoGamesList

import com.projach.videogametracker.domain.models.VideoGameModel

data class VideoGamesListUiState(
    val videoGames: List<VideoGameModel>? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)
