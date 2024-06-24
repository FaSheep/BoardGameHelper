package com.fasheep.boardgamehelper.data

import com.fasheep.boardgamehelper.api.OnlineQRCodeService
import com.fasheep.boardgamehelper.core.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object OnlineQRCodeRepository {

    private const val BASE_URL = "http://bghhk.fasheep.xyz/web/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val onlineQRCodeService = retrofit.create(OnlineQRCodeService::class.java)

    suspend fun addRoom(room: Room): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = onlineQRCodeService.addRoom(room).execute()
                if (response.isSuccessful) {
                    Result.success(response.body() ?: "")
                } else {
                    Result.failure(Exception("HTTP error code: ${response.code()}"))
                }
            } catch (e: IOException) {
                Result.failure(e)
            }
        }
    }
}
