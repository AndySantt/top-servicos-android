package com.top.example.chatbadge

import android.content.Context
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale


object ChatBadgeStore {
    private const val PREF = "chat_badge"
    private const val LEGACY_PREFIX = "lr_"
    private fun scopedKey(userId: String, convId: String) = "lr_${userId}_${convId}"


    fun getLastRead(ctx: Context, convId: String, myUserId: String?): String? {
        val sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        if (!myUserId.isNullOrBlank()) {
            val k = scopedKey(myUserId, convId)
            val scoped = sp.getString(k, null)
            if (scoped != null) return scoped
            // migra legado se existir
            val legacy = sp.getString("$LEGACY_PREFIX$convId", null)
            if (legacy != null) {
                sp.edit().putString(k, legacy).apply()
                return legacy
            }
            return null
        }

        return sp.getString("$LEGACY_PREFIX$convId", null)
    }


    fun setLastRead(ctx: Context, convId: String, iso8601: String, myUserId: String? = null) {
        val sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val e = sp.edit()
        if (!myUserId.isNullOrBlank()) e.putString(scopedKey(myUserId, convId), iso8601)
        e.putString("$LEGACY_PREFIX$convId", iso8601)
        e.apply()
    }

    fun clearAll(ctx: Context) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().clear().apply()
    }
}

/** Parse robusto de timestamps. Evita `when(fmt)` para não gerar switch. */
private fun parseInstantFlexible(raw: String?): Instant? {
    if (raw.isNullOrBlank()) return null


    try { return Instant.parse(raw) } catch (_: Throwable) {}


    try { return OffsetDateTime.parse(raw, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant() } catch (_: Throwable) {}

    try { return ZonedDateTime.parse(raw, DateTimeFormatter.ISO_ZONED_DATE_TIME).toInstant() } catch (_: Throwable) {}

    try {
        val ldt = LocalDateTime.parse(raw, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        return ldt.atZone(ZoneId.systemDefault()).toInstant()
    } catch (_: Throwable) {}

    try {
        val ld = LocalDate.parse(raw, DateTimeFormatter.ISO_LOCAL_DATE)
        return ld.atStartOfDay(ZoneId.systemDefault()).toInstant()
    } catch (_: Throwable) {}


    val candidates = listOf(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale("pt", "BR")),
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale("pt", "BR"))
    )
    for (fmt in candidates) {
        try {
            val ldt = LocalDateTime.parse(raw, fmt)
            return ldt.atZone(ZoneId.systemDefault()).toInstant()
        } catch (_: DateTimeParseException) {  }
    }

    return null
}


fun <T> unseenChatIds(
    ctx: Context,
    items: List<T>,
    id: (T) -> String,
    lastAt: (T) -> String?
): Set<String> = unseenChatIds(ctx, items, id, lastAt, lastFrom = { null }, myId = null)


fun <T> unseenChatIds(
    ctx: Context,
    items: List<T>,
    id: (T) -> String,
    lastAt: (T) -> String?,
    lastFrom: (T) -> String?,
    myId: String?
): Set<String> {
    val res = mutableSetOf<String>()
    for (row in items) {
        val convId = id(row)
        val lastAtStr = lastAt(row) ?: continue


        if (!myId.isNullOrBlank()) {
            val from = lastFrom(row)?.trim()
            if (!from.isNullOrBlank() && from.equals(myId.trim(), ignoreCase = true)) {
                continue
            }
        }

        val lrStr = ChatBadgeStore.getLastRead(ctx, convId, myId)
        val la = parseInstantFlexible(lastAtStr)
        val lr = parseInstantFlexible(lrStr)

        val isUnseen = when {
            la == null && lr == null -> lastAtStr.isNotBlank()
            la == null -> false
            lr == null -> true
            else -> la.isAfter(lr)
        }


        val finalIsUnseen = if (!isUnseen && la == null && lr != null && lrStr != null) {
            // tenta ordem lexicográfica (útil se backend envia mesmo layout sempre)
            lastAtStr > lrStr
        } else isUnseen

        if (finalIsUnseen) res += convId
    }
    return res
}


fun Context.setLastRead(convId: String, iso: String, myUserId: String? = null) {
    ChatBadgeStore.setLastRead(this, convId, iso, myUserId)
}
