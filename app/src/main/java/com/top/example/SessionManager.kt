package com.top.example
import android.content.Context
import com.top.example.net.Role

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun save(token: String, role: Role) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_ROLE, role.name)
            .apply()
    }


    fun save(token: String, role: Role, userId: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_ROLE, role.name)
            .putString(KEY_USER_ID, userId)
            .apply()
    }

    fun setUserId(userId: String) {
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }
    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)
    fun getRole(): Role? =
        prefs.getString(KEY_ROLE, null)?.let { runCatching { Role.valueOf(it) }.getOrNull() }

    fun isLoggedIn(): Boolean = !getToken().isNullOrBlank()


    fun getSubscribedTopic(): String? = prefs.getString(KEY_TOPIC, null)
    fun setSubscribedTopic(topic: String?) {
        prefs.edit().putString(KEY_TOPIC, topic).apply()
    }


    fun setSubscriptionSnapshot(
        subActive: Boolean,
        canAccess: Boolean,
        trialEndsAtIso: String? = null,
        trialClaimed: Boolean = false
    ) {
        prefs.edit()
            .putBoolean(KEY_SUB_ACTIVE, subActive)
            .putBoolean(KEY_CAN_ACCESS, canAccess)
            .putString(KEY_TRIAL_ENDS_AT, trialEndsAtIso)
            .putBoolean(KEY_TRIAL_CLAIMED, trialClaimed)
            .apply()
    }

    fun clearSubscriptionSnapshot() {
        prefs.edit()
            .remove(KEY_SUB_ACTIVE)
            .remove(KEY_CAN_ACCESS)
            .remove(KEY_TRIAL_ENDS_AT)
            .remove(KEY_TRIAL_CLAIMED)
            .apply()
    }

    fun getCanAccessCached(): Boolean? =
        if (prefs.contains(KEY_CAN_ACCESS)) prefs.getBoolean(KEY_CAN_ACCESS, false) else null

    fun getSubSnapshotOrNull(): SubSnapshot? {
        if (!prefs.contains(KEY_CAN_ACCESS)) return null
        return SubSnapshot(
            subActive = prefs.getBoolean(KEY_SUB_ACTIVE, false),
            canAccess = prefs.getBoolean(KEY_CAN_ACCESS, false),
            trialEndsAtIso = prefs.getString(KEY_TRIAL_ENDS_AT, null),
            trialClaimed = prefs.getBoolean(KEY_TRIAL_CLAIMED, false)
        )
    }



    fun getRoleOrDefault(): Role =
        getRole() ?: Role.CONTRATANTE


    fun canAccess(): Boolean =
        prefs.getBoolean(KEY_CAN_ACCESS, false)


    fun isSubActive(): Boolean =
        prefs.getBoolean(KEY_SUB_ACTIVE, false)


    fun clear() {
        prefs.edit().clear().apply()
    }

    data class SubSnapshot(
        val subActive: Boolean,
        val canAccess: Boolean,
        val trialEndsAtIso: String?,
        val trialClaimed: Boolean
    )

    private companion object {
        const val PREFS            = "session"
        const val KEY_TOKEN        = "token"
        const val KEY_ROLE         = "role"
        const val KEY_USER_ID      = "user_id"
        const val KEY_TOPIC        = "fcm_topic"


        const val KEY_SUB_ACTIVE   = "sub_active"
        const val KEY_CAN_ACCESS   = "can_access"
        const val KEY_TRIAL_ENDS_AT= "trial_ends_at"
        const val KEY_TRIAL_CLAIMED= "trial_claimed"
    }
}
