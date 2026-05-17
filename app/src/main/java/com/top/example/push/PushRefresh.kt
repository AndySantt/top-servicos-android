package com.top.example.push


import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun PushRefreshEffect(
    onChat: (convId: String?) -> Unit,
    onJobs: () -> Unit,
    onSubs: () -> Unit = {},
    onAny: () -> Unit = {}
) {
    val ctx = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current


    val onChatState by rememberUpdatedState(onChat)
    val onJobsState by rememberUpdatedState(onJobs)
    val onSubsState by rememberUpdatedState(onSubs)
    val onAnyState by rememberUpdatedState(onAny)

    LaunchedEffect(Unit) {
        Log.d("PushRefreshEffect", "Init registerReceiver…")
    }

    DisposableEffect(lifecycleOwner) {
        val filter = IntentFilter().apply {
            addAction(AppFirebaseMessagingService.ACTION_REFRESH_INBOX)
            addAction(AppFirebaseMessagingService.ACTION_REFRESH_JOBS)
            addAction(AppFirebaseMessagingService.ACTION_REFRESH_SUBS)
            addAction(AppFirebaseMessagingService.ACTION_REFRESH_ANY)
        }

        val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action.orEmpty()
                val convId = intent.getStringExtra("convId")
                when (action) {
                    AppFirebaseMessagingService.ACTION_REFRESH_INBOX -> onChat(convId)
                    AppFirebaseMessagingService.ACTION_REFRESH_JOBS -> onJobs()
                    AppFirebaseMessagingService.ACTION_REFRESH_SUBS -> onSubs()
                    AppFirebaseMessagingService.ACTION_REFRESH_ANY -> onAny()
                }
            }
        }


        ContextCompat.registerReceiver(
            ctx,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        onDispose {
            try {
                ctx.unregisterReceiver(receiver)
            } catch (_: Exception) {
            }
        }
    }
}




