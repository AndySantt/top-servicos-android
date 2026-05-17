
package com.top.example.net

import android.content.Context
import com.top.example.SessionManager

object ApiProvider {
    @Volatile private var api: ApiService? = null

    fun get(ctx: Context): ApiService {
        api?.let { return it }


        val sm = SessionManager(ctx.applicationContext)
        val created = ApiService.createFromContext(ctx) { sm.getToken() }
        api = created
        return created
    }


    fun reset() { api = null }




}


