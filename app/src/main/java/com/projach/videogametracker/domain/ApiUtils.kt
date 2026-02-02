package com.projach.videogametracker.domain

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun buildIgdbBodyTopRated(
    limit: Int,
    offset: Int
): RequestBody = """
    fields name, rating, summary, genres.name, cover.url, release_dates.date;
    where rating != null & cover.url != null;
    sort rating desc;
    limit $limit;
    offset $offset;
""".trimIndent()
    .toRequestBody("text/plain".toMediaType())

fun String.toIgdbUrl(): String = "https:$this"