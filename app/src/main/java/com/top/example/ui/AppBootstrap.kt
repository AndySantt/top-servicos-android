package com.top.example.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import com.top.example.notify.Notifier

@Composable
fun AppBootstrap() {
    val ctx = LocalContext.current

    // cria os canais logo no início
    LaunchedEffect(Unit) { Notifier.ensureChannels(ctx) }

    // pede POST_NOTIFICATIONS só no Android 13+
    if (Build.VERSION.SDK_INT >= 33) {
        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { /* opcional: tratar aprovado/negado */ }

        LaunchedEffect(Unit) {
            val granted = ContextCompat.checkSelfPermission(
                ctx, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
