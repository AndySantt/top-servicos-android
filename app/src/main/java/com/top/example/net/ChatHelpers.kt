
package com.top.example.net

suspend fun ApiService.startOrFindChat(withUserId: String): String? {

    runCatching {
        myChats().firstOrNull { it.withUserId == withUserId }?.id
    }.getOrNull()?.let { return it }


    runCatching { startChat(StartChatReq(withUserId)) }
        .getOrNull()?.conversationId
        ?.let { return it }

    
    kotlinx.coroutines.delay(300)
    return runCatching {
        myChats().firstOrNull { it.withUserId == withUserId }?.id
    }.getOrNull()
}
