package com.top.example.data


import android.content.Context
import androidx.datastore.preferences.preferencesDataStore


val Context.seenStore by preferencesDataStore(name = "contratante_home")
