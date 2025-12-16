package com.example.mini_shop_fronted.utils

import com.example.mini_shop_fronted.dto.ApiErrorResponse
import com.google.gson.Gson
import retrofit2.Response

fun <T> Response<T>.parseError(): ApiErrorResponse? {
    return try {
        val errorJson = this.errorBody()?.string()
        if (errorJson != null) {
            Gson().fromJson(errorJson, ApiErrorResponse::class.java)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}
