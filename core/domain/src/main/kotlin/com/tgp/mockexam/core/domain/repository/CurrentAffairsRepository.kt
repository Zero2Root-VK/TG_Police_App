package com.tgp.mockexam.core.domain.repository

import com.tgp.mockexam.core.model.CurrentAffairsPack
import kotlinx.coroutines.flow.Flow

interface CurrentAffairsRepository {
    fun getLatestPacks(limit: Int = 10): Flow<List<CurrentAffairsPack>>
    suspend fun refresh()
}
