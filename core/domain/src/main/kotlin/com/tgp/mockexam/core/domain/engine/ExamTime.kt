package com.tgp.mockexam.core.domain.engine

object ExamTime {
    const val MS_PER_MINUTE = 60_000L

    fun remainingMs(
        startedAtRealtimeMs: Long,
        startedAtEpochMs: Long,
        durationMins: Int,
        pausedAccumulatedMs: Long,
        nowRealtimeMs: Long,
        nowEpochMs: Long,
    ): Long {
        val elapsedMonotonic = nowRealtimeMs - startedAtRealtimeMs
        val elapsed = if (elapsedMonotonic >= 0L) elapsedMonotonic else nowEpochMs - startedAtEpochMs
        val activeElapsed = (elapsed - pausedAccumulatedMs).coerceAtLeast(0L)
        return (durationMins * MS_PER_MINUTE - activeElapsed).coerceAtLeast(0L)
    }
}