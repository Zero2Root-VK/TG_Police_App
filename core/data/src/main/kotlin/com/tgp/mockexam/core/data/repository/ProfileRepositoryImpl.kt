package com.tgp.mockexam.core.data.repository

import com.tgp.mockexam.core.domain.repository.ProfileRepository
import com.tgp.mockexam.core.network.RemoteProfileRow
import com.tgp.mockexam.core.network.TablesClient
import com.tgp.mockexam.core.network.TablesQuery
import javax.inject.Inject
import kotlinx.serialization.json.JsonObject

class ProfileRepositoryImpl @Inject constructor(
    private val tablesClient: TablesClient,
) : ProfileRepository {
    override suspend fun getRole(userId: String): String {
        val rows = tablesClient.getRows(
            table = "profiles",
            queries = listOf(TablesQuery.equal("userId", userId)),
            limit = 1,
        )
        val obj = rows.rows.firstOrNull() as? JsonObject ?: return "student"
        val r = RemoteProfileRow(obj)
        return if (r.role.isBlank()) "student" else r.role.lowercase()
    }
}
