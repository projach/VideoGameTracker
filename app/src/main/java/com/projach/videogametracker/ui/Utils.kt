package com.projach.videogametracker.ui

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun String.changeIgdbSize(size: IgdbImageSize): String =
    this.replace(DEFAULT_IMG_SIZE, size.size)

fun Long.unixTimeToDateTime(): String {
    //use utc as database keeps dates in utc
    return Instant.ofEpochSecond(this)
        .atZone(ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern(DATE_FORMAT))
}

const val DATE_FORMAT = "dd/MM/yyyy"
const val DEFAULT_IMG_SIZE = "t_thumb"