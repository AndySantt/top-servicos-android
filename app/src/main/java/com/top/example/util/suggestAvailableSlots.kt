package com.top.example.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

fun suggestAvailableSlots(
    agenda: List<Instant>,
    date: LocalDate,
    zoneId: ZoneId = ZoneId.systemDefault(),
    startHour: LocalTime = LocalTime.of(8, 0),
    endHour: LocalTime = LocalTime.of(18, 0),
    serviceDurationMinutes: Long = 90,
    maxSuggestions: Int = 5
): List<LocalTime> {

    val occupied = agenda
        .map { it.atZone(zoneId) }
        .filter { it.toLocalDate() == date }
        .map { it.toLocalTime() }
        .sorted()

    val suggestions = mutableListOf<LocalTime>()

    var cursor = startHour

    while (cursor.plusMinutes(serviceDurationMinutes) <= endHour) {

        val conflict = occupied.any { occupiedTime ->
            val occupiedEnd = occupiedTime.plusMinutes(serviceDurationMinutes)

            // conflito se o intervalo se sobrepõe
            cursor < occupiedEnd && occupiedTime < cursor.plusMinutes(serviceDurationMinutes)
        }

        if (!conflict) {
            suggestions.add(cursor)
            if (suggestions.size >= maxSuggestions) break
        }

        cursor = cursor.plusMinutes(serviceDurationMinutes)
    }

    return suggestions
}
