package com.pouyamiralayi.android.datatracker.network

import android.content.Context

object CredentialManager {
    private const val ACCESS_TOKEN = "jwt"
    private const val PREFERENCE_NAME = "auth"

    fun getCredentials(context: Context?): String? {
        return try {
            val sp = context!!.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            sp!!.getString(ACCESS_TOKEN, null)
        } catch (e: Exception) {
            null
        }
    }

    fun saveCredentials(context: Context?, credential: String?) {

        try {
            val sp = context!!.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            sp!!.edit().putString(ACCESS_TOKEN, credential).apply()
        } catch (e: Exception) {

        }

    }

}