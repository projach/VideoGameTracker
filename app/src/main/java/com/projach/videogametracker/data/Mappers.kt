package com.projach.videogametracker.data

import com.projach.videogametracker.data.model.VideoGameDTO
import com.projach.videogametracker.domain.models.VideoGameModel
import com.projach.videogametracker.domain.toIgdbUrl

fun VideoGameDTO.toModel() : VideoGameModel =
    VideoGameModel(
        id = this.id,
        rating = this.rating,
        summary = this.summary,
        name = this.name,
        genres = this.genres?.map { it.name },
        coverUrl = this.cover?.url?.toIgdbUrl(),
        firstReleaseDate = this.releaseDates?.minOfOrNull{ it.date }
    )