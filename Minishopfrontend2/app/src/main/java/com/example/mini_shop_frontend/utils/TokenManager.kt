package com.example.mini_shop_frontend.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object TokenManager {

    private var sharedPrefs: SharedPreferences? = null




    fun initialize(context: Context){
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()


        sharedPrefs = EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )


    }

    fun saveToken(token: String){
        sharedPrefs?.edit()?.putString("jwt_token", token)?.apply()
    }


    fun getToken(): String?{
        return sharedPrefs?.getString("jwt_token", null)
    }


    fun clearToken(){
        sharedPrefs?.edit()?.remove("jwt_token")?.apply()
    }




}