package com.binar.secondhand.storage

import android.content.Context
import androidx.core.content.edit
import com.binar.secondhand.R

interface Storage {
    fun setString(key: String, value: String)
    fun getString(key: String): String?
    fun remove(key: String)
    fun setBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean
}

class SharedPreferencesStorage (context: Context) : Storage {

    private val sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    override fun setString(key: String, value: String) {
        sharedPref.edit {
            putString(key, value)
            apply()
        }
    }

    override fun getString(key: String): String? = sharedPref.getString(key, "")

    override fun remove(key: String) {
        sharedPref.edit {
            remove(key)
            apply()
        }
    }

    override fun setBoolean(key: String, value: Boolean) {
        sharedPref.edit {
            setBoolean(key, value)
        }
    }

    override fun getBoolean(key: String): Boolean = sharedPref.getBoolean(key, false)
}