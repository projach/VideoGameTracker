package com.projach.videogametracker.data.model

import com.google.gson.annotations.SerializedName

data class VideoGameDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("genres") val genres: List<GenreDTO>?,
    @SerializedName("cover") val cover: VideoGameCoverDTO?,
    @SerializedName("release_dates") val releaseDates: List<ReleaseDatesDTO>?
)

data class ReleaseDatesDTO(
    @SerializedName("date") val date: Long
)

data class GenreDTO (
    @SerializedName("name") val name: String
)

data class VideoGameCoverDTO(
    @SerializedName("url") val url: String
)
