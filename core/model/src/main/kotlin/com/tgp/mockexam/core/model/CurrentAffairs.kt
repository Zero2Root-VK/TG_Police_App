package com.tgp.mockexam.core.model

@kotlinx.serialization.Serializable
data class CurrentAffairsQuestion(
    val id: String,
    val prompt: String,
    val options: List<String>,
    val correct: String, // A-D
    val explanation: String = "",
    val topic: String = "",
)

@kotlinx.serialization.Serializable
data class CurrentAffairsPack(
    val id: String,
    val title: String,
    val dateIso: String, // YYYY-MM-01T00:00:00.000Z or similar
    val subject: String = "Telangana Police",
    val access: String = "free",
    val questions: List<CurrentAffairsQuestion>,
    val publishedAt: String = "",
)
