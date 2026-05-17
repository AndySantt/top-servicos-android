package com.top.example.push

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.top.example.notify.Notifier
import com.top.example.net.ApiProvider
import com.top.example.SessionManager
import kotlinx.coroutines.*
import com.top.example.net.SavePushTokenReq
import com.top.example.util.JustSentGuard



object AppVisibility { @Volatile var isForeground: Boolean = false }

class AppFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCM-Service"
        const val ACTION_REFRESH_INBOX = "com.top.example.ACTION_REFRESH_INBOX"
        const val ACTION_REFRESH_JOBS  = "com.top.example.ACTION_REFRESH_JOBS"
        const val ACTION_REFRESH_SUBS  = "com.top.example.ACTION_REFRESH_SUBS"
        const val ACTION_REFRESH_ANY   = "com.top.example.ACTION_REFRESH_ANY"

        fun sendTokenToServer(ctx: Context, token: String) {
            val session = SessionManager(ctx)
            val userToken = session.getToken() ?: return
            CoroutineScope(Dispatchers.IO).launch {
                runCatching {
                    ApiProvider.get(ctx).saveMyPushToken(SavePushTokenReq(token))
                }.onFailure {
                    Log.w(TAG, "Falha ao enviar FCM token: ${it.message}")
                }
            }
        }
    }


    private fun Intent.scopeToApp(): Intent = apply { setPackage(packageName) }
    private fun sendUiBroadcast(intent: Intent) {
        if (AppVisibility.isForeground) sendBroadcast(intent.scopeToApp())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Novo token FCM: $token")
        sendTokenToServer(applicationContext, token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data  = message.data ?: emptyMap()
        val type  = data["type"].orEmpty()
        val hasSystemNotif = message.notification != null
        val isFg = AppVisibility.isForeground

        val convId = data["conversationId"] ?: data["convId"]
        val me     = SessionManager(this).getUserId()
        val from   = data["fromUserId"]


        val hasNotif = message.notification != null
        Log.d("FCM-Service", "onMessageReceived hasNotif=$hasNotif data=$data")

        // dedupe por messageId (ok)
        if (com.top.example.util.LastMsg.seen(this, message.messageId)) {
            android.util.Log.d("FCM-Service", "drop dup msgId=${message.messageId}")
            return
        }

        Notifier.ensureChannels(this)


        val isChat = !convId.isNullOrBlank() || type.equals("chat", ignoreCase = true)

        if (isChat) {
            val isEchoFromMe = !from.isNullOrBlank() && !me.isNullOrBlank() && from == me
            val isEchoByGuard = !convId.isNullOrBlank() && JustSentGuard.isLikelyEcho(this, convId)


            if (isEchoFromMe || isEchoByGuard) {
                if (isFg) {
                    sendUiBroadcast(Intent(ACTION_REFRESH_INBOX).putExtra("convId", convId))
                    sendUiBroadcast(Intent(ACTION_REFRESH_ANY))
                }
                return
            }


            val sameChatOpen = (com.top.example.util.AppState.currentOpenChatId == convId)
            if (!sameChatOpen) {
                PushCache.incChat(applicationContext)  // <<-- MOVIDO pra cima e INCONDICIONAL ao FG/BG
                if (isFg) {

                    com.top.example.bus.AppBus.emit(
                        com.top.example.bus.ChatEvent.Incoming(convId ?: "")
                    )
                }
            }


            if (!isFg && !hasSystemNotif) {
                val title = data["withName"].orEmpty().ifBlank { "Nova mensagem" }
                val preview = data["preview"]
                val uniqueId = (message.messageId ?: ("chat-" + (convId ?: ""))).hashCode()
                Notifier.newChatMessageUnique(this, uniqueId, title, preview)
            }


            if (isFg) {
                sendUiBroadcast(Intent(ACTION_REFRESH_INBOX).putExtra("convId", convId))
                sendUiBroadcast(Intent(ACTION_REFRESH_ANY))
            }
            return
        }


        when (type) {


            "product_order_new" -> {
                if (!isFg) {
                    Notifier.generic(
                        this,
                        "Novo pedido de produtos",
                        "Você recebeu um novo pedido de compra."
                    )
                }
                if (isFg) {
                    sendUiBroadcast(Intent(ACTION_REFRESH_ANY))
                }
            }

            "product_order_accepted" -> {
                val orderId = data["orderId"] ?: return

                if (!isFg) {
                    Notifier.generic(
                        ctx = this,
                        title = "Pedido aceito",
                        body = "Seu pedido foi aceito. Toque para pagar.",
                        pendingIntent = Notifier.openOrderPendingIntent(this, orderId)
                    )
                }

                if (isFg) {
                    sendUiBroadcast(Intent(ACTION_REFRESH_ANY))
                }
            }




            "product_order_declined" -> {
                if (!isFg) {
                    Notifier.generic(
                        this,
                        "Pedido recusado",
                        "O fornecedor recusou seu pedido."
                    )
                }
                if (isFg) {
                    sendUiBroadcast(Intent(ACTION_REFRESH_ANY))
                }
            }

            "product_order_on_route" -> {
                if (!isFg) {
                    Notifier.generic(
                        this,
                        "Pedido em rota",
                        "Seu pedido saiu para entrega 🚚"
                    )
                }
                if (isFg) {
                    sendUiBroadcast(Intent(ACTION_REFRESH_ANY))
                }
            }

            "product_order_finished" -> {
                if (!isFg) {
                    Notifier.generic(
                        this,
                        "Pedido finalizado",
                        "Seu pedido foi finalizado com sucesso."
                    )
                }
                if (isFg) {
                    sendUiBroadcast(Intent(ACTION_REFRESH_ANY))
                }
            }

            "job", "job_new", "job_created" -> {
                if (!isFg) {
                    Notifier.newJob(this, data["howMany"]?.toIntOrNull() ?: 1)
                }
                if (isFg) {
                    sendUiBroadcast(Intent(ACTION_REFRESH_JOBS))
                    sendUiBroadcast(Intent(ACTION_REFRESH_ANY))
                }
            }
            "job_accepted" -> {
                if (!isFg) {
                    val caregiverName = data["caregiverName"] ?: "Profissional"
                    Notifier.generic(this, "Pedido aceito",
                        "$caregiverName aceitou sua solicitação. Toque para ver.")
                }
                if (isFg) {
                    sendUiBroadcast(Intent(ACTION_REFRESH_JOBS))
                    sendUiBroadcast(Intent(ACTION_REFRESH_ANY))
                }
            }

            "follow" -> {
                val followerName = data["followerName"] ?: "Alguém"
                val title = "Novo seguidor"
                val body = "$followerName começou a seguir você"

                if (!isFg && !hasSystemNotif) {
                    Notifier.generic(this, title, body)
                }
                if (isFg) {
                    sendUiBroadcast(Intent(ACTION_REFRESH_ANY))
                }
            }


            else -> {
                if (!isFg && !hasSystemNotif) {
                    Notifier.generic(this, data["title"] ?: "Notificação", data["body"])
                }
                if (isFg) sendUiBroadcast(Intent(ACTION_REFRESH_ANY))
            }




        }
    }
}




object PushCache {
    private const val PREFS = "push_cache"
    private const val KEY_PENDING_CHAT = "pending_chat_count"

    fun incChat(ctx: Context) {
        val sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val cur = sp.getInt(KEY_PENDING_CHAT, 0)
        sp.edit().putInt(KEY_PENDING_CHAT, cur + 1).apply()
    }

    fun getChat(ctx: Context): Int {
        return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getInt(KEY_PENDING_CHAT, 0)
    }

    fun clearChat(ctx: Context) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putInt(KEY_PENDING_CHAT, 0).apply()
    }
}
