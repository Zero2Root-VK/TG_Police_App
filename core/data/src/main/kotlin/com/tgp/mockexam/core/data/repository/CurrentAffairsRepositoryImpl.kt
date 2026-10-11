package com.tgp.mockexam.core.data.repository

import com.tgp.mockexam.core.domain.repository.CurrentAffairsRepository
import com.tgp.mockexam.core.model.CurrentAffairsPack
import com.tgp.mockexam.core.model.CurrentAffairsQuestion
import com.tgp.mockexam.core.network.RemoteDailyCaRow
import com.tgp.mockexam.core.network.TablesClient
import com.tgp.mockexam.core.network.TablesQuery
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class CurrentAffairsRepositoryImpl @Inject constructor(
    private val tablesClient: TablesClient,
) : CurrentAffairsRepository {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }
    private val _packs = MutableStateFlow<List<CurrentAffairsPack>>(emptyList())

    override fun getLatestPacks(limit: Int): Flow<List<CurrentAffairsPack>> = _packs.asStateFlow()

    override suspend fun refresh() {
        val rows = tablesClient.getRows(
            table = "daily_ca",
            queries = listOf(TablesQuery.orderDesc("date")),
            limit = 50,
        )
        val packs = rows.rows.mapNotNull { obj ->
            val r = RemoteDailyCaRow(obj as JsonObject)
            if (r.id.isBlank()) return@mapNotNull null
            try {
                val questions = parseQuestions(r.questionsJson)
                CurrentAffairsPack(
                    id = r.id,
                    title = formatTitle(r.date, r.subject),
                    dateIso = r.date,
                    subject = r.subject,
                    access = r.access,
                    questions = questions,
                    publishedAt = r.publishedAt,
                )
            } catch (t: Throwable) {
                null
            }
        }
        _packs.value = packs
    }

    private fun formatTitle(dateIso: String, subject: String): String {
        if (dateIso.isNotBlank() && dateIso.length >= 7) {
            val y = dateIso.substring(0,4)
            val m = dateIso.substring(5,7)
            val month = when(m) {
                "01"->"Jan";"02"->"Feb";"03"->"Mar";"04"->"Apr";"05"->"May";"06"->"Jun";
                "07"->"Jul";"08"->"Aug";"09"->"Sep";"10"->"Oct";"11"->"Nov";"12"->"Dec"; else -> m
            }
            return "Current Affairs · $month $y"
        }
        return if (subject.isNotBlank()) "Current Affairs · $subject" else "Current Affairs"
    }

    private fun parseQuestions(qjson: String): List<CurrentAffairsQuestion> {
        if (qjson.isBlank()) return emptyList()
        return try {
            val el = json.parseToJsonElement(qjson)
            val arr = when (el) {
                is JsonArray -> el
                is JsonObject -> el.jsonObject["questions"]?.jsonArray ?: JsonArray(emptyList())
                else -> JsonArray(emptyList())
            }
            arr.mapNotNull { je ->
                val obj = je as? JsonObject ?: return@mapNotNull null
                val id = obj["id"]?.jsonPrimitive?.content ?: obj["qId"]?.jsonPrimitive?.content ?: ""
                val prompt = obj["prompt"]?.jsonPrimitive?.content ?: obj["question"]?.jsonPrimitive?.content ?: ""
                val correct = obj["correct"]?.jsonPrimitive?.content ?: obj["answer"]?.jsonPrimitive?.content ?: ""
                val explanation = obj["explanation"]?.jsonPrimitive?.content ?: ""
                val topic = obj["topic"]?.jsonPrimitive?.content ?: ""
                val options = obj["options"]?.jsonArray?.mapNotNull { o ->
                    when (o) {
                        is JsonObject -> {
                            o["text"]?.jsonPrimitive?.content ?: o["label"]?.jsonPrimitive?.content ?: o.jsonPrimitiveOrNull()
                        }
                        is kotlinx.serialization.json.JsonPrimitive -> o.content
                        else -> null
                    }
                } ?: emptyList()
                if (prompt.isBlank()) null else CurrentAffairsQuestion(
                    id = id,
                    prompt = prompt,
                    options = options,
                    correct = correct,
                    explanation = explanation,
                    topic = topic,
                )
            }
        } catch (_: Throwable) {
            emptyList()
        }
    }
    private fun JsonObject.jsonPrimitiveOrNull(): String? = this.values.firstOrNull()?.jsonPrimitive?.content
}
