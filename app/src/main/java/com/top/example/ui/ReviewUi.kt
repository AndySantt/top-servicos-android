package com.top.example.ui


import android.app.Activity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import com.top.example.util.ReviewPrefs
import com.top.example.util.tryRequestInAppReview
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp


@Composable
fun MaybeAskForReview(
    activity: Activity,
    minOpens: Int = 7,
    minActions: Int = 3
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        ReviewPrefs.incrementOpen(ctx)

        if (ReviewPrefs.shouldAskForReview(ctx, minOpens = minOpens, minActions = minActions)) {
            showDialog = true
        }
    }

    if (showDialog) {
        RatePromptDialog(
            onConfirm = {

                scope.launch {
                    try {
                        tryRequestInAppReview(activity)
                    } catch (e: Exception) {

                    } finally {

                        ReviewPrefs.markAsked(ctx)
                        showDialog = false
                    }
                }
            },
            onLater = {

                ReviewPrefs.markAsked(ctx)
                showDialog = false
            },
            onDontAsk = {
                ReviewPrefs.setDontAskAgain(ctx, true)
                showDialog = false
            },
            onDismiss = {

                ReviewPrefs.markAsked(ctx)
                showDialog = false
            }
        )
    }
}


@Composable
fun RatePromptDialog(
    onConfirm: () -> Unit,
    onLater: () -> Unit,
    onDontAsk: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(),
        title = { Text("Gostou do app?") },
        text = { Text("Se você estiver gostando, nos ajude avaliando o app na Play Store. Isso nos ajuda muito!") },
        confirmButton = { TextButton(onClick = onConfirm) { Text("Avaliar agora") } },
        dismissButton = {
            Row {
                TextButton(onClick = onLater) { Text("Lembrar depois") }
                Spacer(modifier = androidx.compose.ui.Modifier.width(8.dp))
                TextButton(onClick = onDontAsk) { Text("Não perguntar de novo") }
            }
        }
    )
}
