package com.tgp.mockexam.core.domain.repository

interface ProfileRepository {
    suspend fun getRole(userId: String): String
}
