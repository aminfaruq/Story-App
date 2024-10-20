package com.aminfaruq.storyapp.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("StoryAppPreferences", Context.MODE_PRIVATE)

    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
        private const val NAME_KEY = "NAME_KEY"

    }

    // Simpan token
    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun saveName(name: String) {
        sharedPreferences.edit().putString(NAME_KEY, name).apply()
    }

    // Ambil token
    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    fun getName(): String? {
        return sharedPreferences.getString(NAME_KEY, null)
    }
    // Hapus token
    fun clearToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
        sharedPreferences.edit().remove(NAME_KEY).apply()
    }
}