package com.tgp.mockexam.core.data.sync

/** Result of a single content sync pass. */
sealed interface SyncResult {
    data object Success : SyncResult
    data class Failure(val throwable: Throwable) : SyncResult
}

enum class SyncState {
    IDLE,
    IN_PROGRESS,
}