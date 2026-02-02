package com.projach.videogametracker.data.model

import com.google.gson.annotations.SerializedName

data class TwitchAuthDTO(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Long
)
