package com.tgp.mockexam.core.network

/** Read-only client for the Appwrite TablesDB REST API (content tables, guest-readable). */
interface TablesClient {
    /**
     * Fetches a page of rows. [queries] are encoded as `queries[]` params;
     * pass [TablesQuery.offset] inside [queries] for pagination.
     */
    suspend fun getRows(
        table: String,
        queries: List<TablesQuery> = emptyList(),
        limit: Int = 25,
    ): RemoteRows
}

class TablesClientException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)