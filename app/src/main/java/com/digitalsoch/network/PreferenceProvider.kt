package com.digitalsoch.network

import android.content.Context
import android.content.SharedPreferences

class PreferenceProvider(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("digitalsoch", Context.MODE_PRIVATE)

    fun putBoolean(key: String, value: Boolean?) {
        if (value != null) {
            preferences.edit().putBoolean(key, value).apply()
        } else {
            preferences.edit().putBoolean(key, false).apply()
        }
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun getLong(key: String): Long {
        return preferences.getLong(key, 0)
    }

    fun putLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    fun putString(key: String, value: String?) {
        preferences.edit().putString(key, value).apply()
    }

    fun putInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun getString(key: String): String? {
        return preferences.getString(key, "")
    }

    fun getInt(key: String): Int {
        return preferences.getInt(key, 0)
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

    fun hasKey(key: String): Boolean {
        return preferences.contains(key)
    }

    fun removeData(key: String) {
        preferences.edit().remove(key).apply()
    }
}