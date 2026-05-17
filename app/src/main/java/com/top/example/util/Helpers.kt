package com.top.example.util

import android.content.Context
import com.top.example.net.ConversationSummary
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.TemporalAccessor


// ---- LastRead helpers (timestamp ISO simples via SharedPreferences) ----
private const val PREFS = "chat_prefs"
private fun keyLastRead(id: String) = "last_read_$id"

// ISO flexível: 2025-10-06T12:34:56[.SSS][Z|+00:00]
private val FLEX_ISO: DateTimeFormatter = DateTimeFormatterBuilder()
    .append(DateTimeFormatter.ISO_LOCAL_DATE)        // yyyy-MM-dd
    .appendLiteral('T')
    .appendValue(java.time.temporal.ChronoField.HOUR_OF_DAY, 2)
    .appendLiteral(':')
    .appendValue(java.time.temporal.ChronoField.MINUTE_OF_HOUR, 2)
    .appendLiteral(':')
    .appendValue(java.time.temporal.ChronoField.SECOND_OF_MINUTE, 2)
    .optionalStart().appendFraction(java.time.temporal.ChronoField.NANO_OF_SECOND, 1, 9, true).optionalEnd()
    .optionalStart().appendOffsetId().optionalEnd()  // +00:00
    .optionalStart().appendLiteral('Z').optionalEnd()// Z
    .toFormatter()

fun parseInstantSafe(s: String?): Instant? {
    if (s.isNullOrBlank()) return null
    return try {
        // tenta primeiro ISO “com fuso”
        Instant.parse(s)
    } catch (_: Exception) {
        try {
            // tenta com builder flexível; se não houver fuso, assume UTC
            val t: TemporalAccessor = FLEX_ISO.parse(s)
            return when {
                t.isSupported(java.time.temporal.ChronoField.OFFSET_SECONDS) ->
                    OffsetDateTime.from(t).toInstant()
                else -> LocalDateTime.from(t).toInstant(ZoneOffset.UTC)
            }
        } catch (_: Exception) {
            try {
                // fallback para "yyyy-MM-dd HH:mm:ss"
                val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                LocalDateTime.parse(s, fmt).toInstant(ZoneOffset.UTC)
            } catch (_: Exception) {
                null
            }
        }
    }
}


// ===== persistência de "lido até" por conversa =====
private const val SP_LAST_READ = "chat_last_read"

fun Context.setLastRead(convId: String, isoInstant: String) {
    getSharedPreferences(SP_LAST_READ, Context.MODE_PRIVATE)
        .edit()
        .putString("last_$convId", isoInstant)
        .apply()
}

fun Context.getLastRead(convId: String): String? {
    return getSharedPreferences(SP_LAST_READ, Context.MODE_PRIVATE)
        .getString("last_$convId", null)
}

// ===== contagem base de não lidas (sem autor) =====
fun calcUnreadOnly(ctx: Context, newList: List<ConversationSummary>): Int {
    var unread = 0
    for (c in newList) {
        val lastRead = parseInstantSafe(ctx.getLastRead(c.id))
        val lastAt   = parseInstantSafe(c.lastAt)
        if (lastAt != null && (lastRead == null || lastAt.isAfter(lastRead))) {
            unread++
        }
    }
    return unread
}




/** Conta quantas conversas têm mensagem mais nova que o último 'read'. */



object LastMsg {
    private const val PREFS = "last_msg"
    private const val KEY   = "id"

    /** Retorna true se esse messageId já foi visto (e grava o atual). */
    fun seen(ctx: android.content.Context, id: String?): Boolean {
        if (id.isNullOrBlank()) return false
        val sp   = ctx.getSharedPreferences(PREFS, android.content.Context.MODE_PRIVATE)
        val prev = sp.getString(KEY, null)
        sp.edit().putString(KEY, id).apply()
        return prev == id
    }
}