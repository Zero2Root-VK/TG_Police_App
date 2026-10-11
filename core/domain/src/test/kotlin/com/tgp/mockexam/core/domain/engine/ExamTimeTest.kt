package com.tgp.mockexam.core.domain.engine

import org.junit.Assert.assertEquals
import org.junit.Test

class ExamTimeTest {

    private val nowRealtime = 5_000_000L
    private val nowEpoch = 1_700_000_000_000L

    @Test
    fun `full duration remaining at start`() {
        val remaining = ExamTime.remainingMs(
            startedAtRealtimeMs = nowRealtime,
            startedAtEpochMs = nowEpoch,
            durationMins = 60,
            pausedAccumulatedMs = 0L,
            nowRealtimeMs = nowRealtime,
            nowEpochMs = nowEpoch,
        )
        assertEquals(60 * 60_000L, remaining)
    }

    @Test
    fun `elapsed time is subtracted from monotonic clock`() {
        val remaining = ExamTime.remainingMs(
            startedAtRealtimeMs = nowRealtime,
            startedAtEpochMs = nowEpoch,
            durationMins = 1,
            pausedAccumulatedMs = 0L,
            nowRealtimeMs = nowRealtime + 10_000L,
            nowEpochMs = nowEpoch,
        )
        assertEquals(50_000L, remaining)
    }

    @Test
    fun `paused time is not counted against the candidate`() {
        val remaining = ExamTime.remainingMs(
            startedAtRealtimeMs = nowRealtime,
            startedAtEpochMs = nowEpoch,
            durationMins = 1,
            pausedAccumulatedMs = 30_000L,
            nowRealtimeMs = nowRealtime + 60_000L,
            nowEpochMs = nowEpoch,
        )
        assertEquals(30_000L, remaining)
    }

    @Test
    fun `after reboot falls back to epoch clock`() {
        val remaining = ExamTime.remainingMs(
            startedAtRealtimeMs = nowRealtime,
            startedAtEpochMs = nowEpoch,
            durationMins = 1,
            pausedAccumulatedMs = 0L,
            nowRealtimeMs = nowRealtime - 500L,
            nowEpochMs = nowEpoch + 20_000L,
        )
        assertEquals(40_000L, remaining)
    }

    @Test
    fun `remaining never goes below zero`() {
        val remaining = ExamTime.remainingMs(
            startedAtRealtimeMs = nowRealtime,
            startedAtEpochMs = nowEpoch,
            durationMins = 1,
            pausedAccumulatedMs = 0L,
            nowRealtimeMs = nowRealtime + 10 * 60_000L,
            nowEpochMs = nowEpoch,
        )
        assertEquals(0L, remaining)
    }
}