package com.jinproject.features.core.utils

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap

fun <T, R> List<T>.mapToImmutableList(transForm: (T) -> R): ImmutableList<R> =
    this@mapToImmutableList.map { transForm(it) }.toImmutableList()

fun <T, V, RT, RV> Map<T, V>.putAllToImmutableMap(transForm: (Map.Entry<T,V>) -> Pair<RT,RV>): ImmutableMap<RT, RV> =
    mutableMapOf<RT, RV>().apply {
        this@putAllToImmutableMap.forEach { item ->
            val pair = transForm(item)
            put(pair.first, pair.second)
        }
    }.toImmutableMap()