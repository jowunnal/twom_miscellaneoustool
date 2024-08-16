package com.jinproject.design_compose.component.lazyList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun rememberTimeScheduler(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): TimeScheduler {
    val state = remember {
        TimeScheduler(coroutineScope)
    }

    return state
}

/**
 * Time 이 셋팅된 후, execute() 를 수행시키면, 셋팅된 Time 이 감소하여 0이 되도록 만드는 타이머 클래스
 * 셋팅된 시간이 0이 되면, 동작하지 않음을 의미함
 *
 * 예시: 드래그가 요구치 만큼 발생하면, 타이머가 셋팅되면서 뷰가 보이고 타이머가 0이되어 종료되면 뷰가 보이지 않음
 */
@Stable
class TimeScheduler(
    private val scope: CoroutineScope,
    private val callBack: suspend () -> Unit = {},
) {
    private var scheduledTime by mutableLongStateOf(0L)

    val isRunning by derivedStateOf {
        scheduledTime > 0L
    }

    private var job: Job? = null

    private fun execute() = scope.launch(context = Dispatchers.Default) {
        while (scheduledTime > 0L) {
            delay(1000L)
            scheduledTime -= 1000L
        }
        callBack()
        job = null
    }

    fun setTime(minimumExecutingTime: Long = STANDARD_MILLIS) {
        scheduledTime = minimumExecutingTime

        job = if (job == null) {
            execute()
        } else {
            if (job!!.isActive)
                job!!.cancel()

            execute()
        }
    }

    fun cancel() {
        if (job != null && job!!.isActive) {
            job!!.cancel()
            job = null
            scheduledTime = 0
        }
    }

    companion object {
        const val STANDARD_MILLIS = 3000L
    }
}