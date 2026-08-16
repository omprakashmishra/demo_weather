package com.collabera.weather.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MySharedPreference @Inject constructor(@ApplicationContext val context: Context){

    private val APP_PREFERENCE = "DemoApp"
    private val APP_FIREBASE_PREFERENCE = "DemoApp_Firebase_Preference"
    private val FIREBASE_TOKEN = "Firebase_token"

    private val sharedPreferences = context.getSharedPreferences(APP_PREFERENCE, Context.MODE_PRIVATE)
    private val sharedPreferencesFirebase = context.getSharedPreferences(APP_FIREBASE_PREFERENCE, Context.MODE_PRIVATE)


    fun setString( key: String, value: String?) {
       if (key == FIREBASE_TOKEN) {
           sharedPreferencesFirebase.edit().putString(key, value).apply()
        } else {
           sharedPreferences.edit().putString(key, value).apply()
        }
    }

    fun getString( key: String): String? {
        return if (key == FIREBASE_TOKEN) {
            sharedPreferencesFirebase.getString(key,"")
        } else {
            sharedPreferences.getString(key,"")
        }
    }

    fun setBoolean( key: String?, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean( key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun setFloat( key: String?, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    fun getFloat( key: String?): Float {
        return sharedPreferences.getFloat(key, 0f)
    }

    fun clearSharedPreference() {
         sharedPreferences.edit().clear().apply()
    }

}