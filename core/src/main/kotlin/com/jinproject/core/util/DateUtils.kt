package com.jinproject.core.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun ZonedDateTime.toMeridiem() = format(DateTimeFormatter.ofPattern("a"))
fun ZonedDateTime.to12Hour() = format(DateTimeFormatter.ofPattern("hh"))
fun LocalDateTime.toEpochMilli() = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
fun ZonedDateTime.toEpochMilli() = toInstant().toEpochMilli()