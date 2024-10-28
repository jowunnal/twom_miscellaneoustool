package com.jinproject.core.util

fun <T> T.runIf(predicate: Boolean, block: T.() -> T): T = if(predicate) block() else this