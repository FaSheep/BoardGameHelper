package com.fasheep.boardgamehelper.api

import com.fasheep.boardgamehelper.core.Room
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OnlineQRCodeService {
    @POST("add.do")
    fun addRoom(@Body room: Room): Call<String>
}
