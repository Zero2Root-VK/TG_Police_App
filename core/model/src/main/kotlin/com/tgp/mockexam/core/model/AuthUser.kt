package com.tgp.mockexam.core.model

/** A signed-in account (Appwrite `account` user projection). */
data class AuthUser(
    val id: String,
    val name: String,
    val email: String,
)
