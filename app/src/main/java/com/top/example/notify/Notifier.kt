
package com.top.example.notify

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.top.example.R
import com.top.example.MainActivity






object Notifier {

    private const val CHANNEL_CHAT = "chat.v2"
    private const val CHANNEL_JOBS = "jobs.v2"

    fun ensureChannels(ctx: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            fun make(id: String, name: String, importance: Int) =
                NotificationChannel(id, name, importance).apply {
                    setSound(
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                        AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
                    )
                    enableVibration(true)
                    setShowBadge(true)
                    description = name
                }


            nm.createNotificationChannel(make(CHANNEL_CHAT, "Mensagens", NotificationManager.IMPORTANCE_HIGH))
            nm.createNotificationChannel(make(CHANNEL_JOBS, "Pedidos",   NotificationManager.IMPORTANCE_HIGH))
        }
    }

    private fun canPost(ctx: Context): Boolean {
        val ok = if (Build.VERSION.SDK_INT >= 33) {
            ContextCompat.checkSelfPermission(ctx, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(ctx).areNotificationsEnabled()
        }
        if (!ok) android.util.Log.w("Notifier", "Sem permissão p/ notificar (POST_NOTIFICATIONS ou notificações desativadas)")
        return ok
    }

    private fun appLaunchPendingIntent(
        ctx: Context,
        intent: Intent? = null
    ): PendingIntent {
        val launchIntent = intent ?: ctx.packageManager
            .getLaunchIntentForPackage(ctx.packageName)
        ?: Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            setPackage(ctx.packageName)
        }

        val flags = if (Build.VERSION.SDK_INT >= 23)
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        else PendingIntent.FLAG_UPDATE_CURRENT

        return PendingIntent.getActivity(ctx, 0, launchIntent, flags)
    }


    @SuppressLint("MissingPermission")
    fun newChatMessage(ctx: Context, convId: String, title: String, preview: String?) {
        newChatMessageUnique(ctx, ("chat-$convId").hashCode(), title, preview)
    }


    @SuppressLint("MissingPermission")
    fun newChatMessageUnique(ctx: Context, uniqueId: Int, title: String, preview: String?) {
        if (!canPost(ctx)) return
        ensureChannels(ctx)

        val notif = NotificationCompat.Builder(ctx, CHANNEL_CHAT)
            .setSmallIcon(R.drawable.ic_stat_facility)
            .setContentTitle(title.ifBlank { "Nova mensagem" })
            .setContentText(preview ?: "Nova mensagem")
            .setStyle(NotificationCompat.BigTextStyle().bigText(preview ?: "Nova mensagem"))
            .setAutoCancel(true)
            .setContentIntent(appLaunchPendingIntent(ctx))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .build()

        NotificationManagerCompat.from(ctx).notify(uniqueId, notif)
    }

    @SuppressLint("MissingPermission")
    fun newJob(ctx: Context, howMany: Int) {
        if (!canPost(ctx)) return
        ensureChannels(ctx)

        val notif = NotificationCompat.Builder(ctx, CHANNEL_JOBS)
            .setSmallIcon(R.drawable.ic_stat_notify)
            .setContentTitle("Novo pedido")
            .setContentText("Você tem $howMany novo(s) pedido(s).")
            .setAutoCancel(true)
            .setContentIntent(appLaunchPendingIntent(ctx))
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .build()

        NotificationManagerCompat.from(ctx).notify("jobs".hashCode(), notif)
    }


    @SuppressLint("MissingPermission")
    fun generic(
        ctx: Context,
        title: String,
        body: String?,
        pendingIntent: PendingIntent? = null
    ) {
        if (!canPost(ctx)) return
        ensureChannels(ctx)

        val notif = NotificationCompat.Builder(ctx, CHANNEL_JOBS)
            .setSmallIcon(R.drawable.ic_stat_notify)
            .setContentTitle(title.ifBlank { "Notificação" })
            .setContentText(body ?: "")
            .setStyle(NotificationCompat.BigTextStyle().bigText(body ?: ""))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent ?: appLaunchPendingIntent(ctx))
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .build()

        NotificationManagerCompat
            .from(ctx)
            .notify(("generic-" + title + body).hashCode(), notif)
    }


    fun openOrderPendingIntent(
        ctx: Context,
        orderId: String
    ): PendingIntent {

        val intent = Intent(ctx, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("route", "product-order")
            putExtra("orderId", orderId)
        }

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        return PendingIntent.getActivity(
            ctx,
            orderId.hashCode(),
            intent,
            flags
        )
    }


}
