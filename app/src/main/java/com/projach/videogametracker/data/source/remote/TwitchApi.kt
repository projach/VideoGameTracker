package com.projach.videogametracker.data.source.remote

import com.projach.videogametracker.data.model.TwitchAuthDTO
import com.projach.videogametracker.domain.POST_TWITCH_TOKEN
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface TwitchApi {
    @POST(POST_TWITCH_TOKEN)
    suspend fun getAuthToken(
        @Query("client_secret") clientSecret: String,
        @Query("client_id") clientId: String,
        @Query("grant_type") grantType: String
    ): Response<TwitchAuthDTO>
}