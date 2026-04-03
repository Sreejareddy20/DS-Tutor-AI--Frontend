package com.simats.dstutorai

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("ds_tutor_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_PREMIUM_USER = "is_premium_user"
    }

    fun setPremiumUser(isPremium: Boolean) {
        prefs.edit().putBoolean(KEY_IS_PREMIUM_USER, isPremium).apply()
    }

    fun isPremiumUser(): Boolean {
        return prefs.getBoolean(KEY_IS_PREMIUM_USER, false)
    }
}
