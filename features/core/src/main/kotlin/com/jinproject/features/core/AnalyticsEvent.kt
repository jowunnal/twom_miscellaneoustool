package com.jinproject.features.core

import com.google.firebase.analytics.ParametersBuilder

sealed interface AnalyticsEvent {
    val eventName: String

    fun logEvent(block: ParametersBuilder)

    data class StartAlarm(
        override val eventName: String = "start_alarm",
        val monsName: String,
        val timeStamp: String
    ) : AnalyticsEvent {
        override fun logEvent(block: ParametersBuilder) {
            block.apply {
                param(MONSTER_NAME, monsName)
                param(TIME_STAMP, timeStamp)
            }
        }
    }

    data object ASCT : AnalyticsEvent {
        override val eventName: String = "always_see_current_time"
        override fun logEvent(block: ParametersBuilder) {

        }
    }

    data class FrequentlyUsedBoss(
        override val eventName: String = "add_frequently_used_boss_to_asct",
        val monsName: String,
    ) : AnalyticsEvent {
        override fun logEvent(block: ParametersBuilder) {
            block.apply {
                param(MONSTER_NAME, monsName)
            }
        }
    }

    data class CollectionSearchWord(
        override val eventName: String = "collection_search_word",
        val word: String,
    ) : AnalyticsEvent {
        override fun logEvent(block: ParametersBuilder) {
            block.apply {
                param(SEARCH_WORD, word)
            }
        }
    }

    data object DropListScreen : AnalyticsEvent {
        override val eventName: String
            get() = "drop_list_screen"

        override fun logEvent(block: ParametersBuilder) {}
    }

    data class SimulatorAddItem(
        override val eventName: String = "simulator_add_item",
        val itemName: String,

        ) : AnalyticsEvent {
        override fun logEvent(block: ParametersBuilder) {
            block.apply {
                param(ITEM_NAME, itemName)
            }
        }
    }

    data class SimulatorEnchant(
        override val eventName: String = "simulator_enchant_item",
        val itemName: String,
        val result: Boolean,

        ) : AnalyticsEvent {
        override fun logEvent(block: ParametersBuilder) {
            block.apply {
                param(ITEM_NAME, itemName)
                param(RESULT, result.toString())
            }
        }
    }

    data object GalleryScreen : AnalyticsEvent {
        override val eventName: String
            get() = "gallery_screen"

        override fun logEvent(block: ParametersBuilder) {}
    }

    companion object {
        const val MONSTER_NAME = "monster_name"
        const val ITEM_NAME = "item_name"
        const val TIME_STAMP = "time_stamp"
        const val SEARCH_WORD = "search_word"
        const val RESULT = "result"
    }
}