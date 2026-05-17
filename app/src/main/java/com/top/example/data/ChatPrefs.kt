package com.top.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private const val DS_NAME = "chat_prefs"
val Context.chatStore: DataStore<Preferences> by preferencesDataStore(DS_NAME)

private fun lastReadKey(convId: String) = stringPreferencesKey("last_read_$convId")

suspend fun Context.setLastRead(convId: String, iso: String) {
    chatStore.edit { it[lastReadKey(convId)] = iso }
}

suspend fun Context.getLastRead(convId: String): String? {
    val pref = chatStore.data.first()
    return pref[lastReadKey(convId)]
}
