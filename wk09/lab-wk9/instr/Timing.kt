package utils

import kotlin.system.measureTimeMillis

/**
 * Helper to measure task duration and log via Logger
 * @param sessionId Anonymous session ID
 * @param taskCode Task identifier (e.g., T3_add)
 * @param jsMode "js-on" or "js-off"
 * @param action The task action to execute and time
 * @return Result of the action
 */
inline fun <T> timeAndLogTask(
    sessionId: String,
    taskCode: String,
    jsMode: String,
    action: () -> T
): T {
    var result: T? = null
    val durationMs = measureTimeMillis {
        result = action()
    }

    // Assume success if no exception; adjust based on actual logic
    Logger.logTaskEvent(
        sessionId = sessionId,
        taskCode = taskCode,
        step = "complete",
        outcome = "success",
        durationMs = durationMs,
        httpStatus = 200,
        jsMode = jsMode
    )

    return result!!
}