package com.jinproject.twomillustratedbook.utils

import java.util.Calendar

var Calendar.year
    get() = get(Calendar.YEAR)
    set(value) {
        set(Calendar.YEAR, value)
    }

var Calendar.month
    get() = get(Calendar.MONTH) + 1
    set(value) {
        set(Calendar.MONTH, value - 1)
    }

var Calendar.date
    get() = get(Calendar.DATE)
    set(value) {
        set(Calendar.DATE, value)
    }

var Calendar.day
    get() = get(Calendar.DAY_OF_WEEK)
    set(value) {
        set(Calendar.DAY_OF_WEEK, value)
    }

var Calendar.hour
    get() = get(Calendar.HOUR_OF_DAY)
    set(value) {
        set(Calendar.HOUR_OF_DAY, value)
    }

var Calendar.minute
    get() = get(Calendar.MINUTE)
    set(value) {
        set(Calendar.MINUTE, value)
    }

var Calendar.second
    get() = get(Calendar.SECOND)
    set(value) {
        set(Calendar.SECOND, value)
    }