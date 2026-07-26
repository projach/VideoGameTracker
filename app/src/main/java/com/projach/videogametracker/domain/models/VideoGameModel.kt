package com.projach.videogametracker.domain.models

data class VideoGameModel(
    val id: Int,
    val rating: Double?,
    val summary: String?,
    val name: String,
    val genres: List<String>?,
    val coverUrl: String?,
    val firstReleaseDate: Long?
)