package utils

import java.io.File
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.UUID

object Logger {
    // Log file path (matches wk06/instr/plan.md)
    private val logFile = File("data/metrics.csv")

    init {
        // Create file with header if it doesn't exist
        if (!logFile.exists()) {
            logFile.parentFile.mkdirs()
            logFile.writeText("ts_iso,session_id,request_id,task_code,step,outcome,ms,http_status,js_mode\n")
        }
    }

    /**
     * Log a task event to CSV
     * @param sessionId Anonymous session ID (6-12 hex chars)
     * @param taskCode Task identifier (e.g., T1_filter, T2_edit)
     * @param step Current step (e.g., submit, validation)
     * @param outcome Result (success, validation_error, timeout)
     * @param durationMs Server-side duration in milliseconds
     * @param httpStatus HTTP response code (200, 400, etc.)
     * @param jsMode "js-on" or "js-off" (from HX-Request header)
     */
    fun logTaskEvent(
        sessionId: String,
        taskCode: String,
        step: String,
        outcome: String,
        durationMs: Long,
        httpStatus: Int,
        jsMode: String
    ) {
        val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        val requestId = "req_${UUID.randomUUID().toString().take(6)}"

        val logLine = listOf(
            timestamp,
            sessionId,
            requestId,
            taskCode,
            step,
            outcome,
            durationMs.toString(),
            httpStatus.toString(),
            jsMode
        ).joinToString(",")

        logFile.appendText("$logLine\n")
    }
}