package com.projach.videogametracker.data.source.remote

import com.projach.videogametracker.data.model.VideoGameDTO
import com.projach.videogametracker.domain.POST_VIDEO_GAMES
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IgdbApi {
    @POST(POST_VIDEO_GAMES)
    suspend fun getVideoGames(
        @Body body: RequestBody
    ): Response<List<VideoGameDTO>>
}