package com.fasheep.boardgamehelper.data

import android.os.Build
import com.fasheep.boardgamehelper.App
import com.fasheep.boardgamehelper.core.Room
import com.fasheep.boardgamehelper.core.RoomManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

object RoomRepository {
    private lateinit var context: App
    private lateinit var job: Job

    fun lateInit(context: App) {
        this.context = context
        loadRoomList()
    }

    private fun loadRoomList() {
        job = CoroutineScope(Job() + Dispatchers.IO).launch {
            val assetManager = context.assets
            val language = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                context.resources.configuration.locales[0].language
            else
                context.resources.configuration.locale.language

            assetManager.list(language)?.forEach { s ->
                val iStream = assetManager.open("${language}/$s")
                val inputStreamReader = InputStreamReader(iStream)
                val reader = BufferedReader(inputStreamReader)
                val stringBuilder = StringBuilder()
                var temp: String?
                while (reader.readLine().also { temp = it } != null) {
                    stringBuilder.append(temp)
                }
                reader.close()
                RoomManager.addRoom(s, Room.getInstanceFromJson(stringBuilder.toString()))
            }
        }
    }

     fun findRoomByName(roomName: String): Room {
        return RoomManager.getRoomList().find { it.roomName == roomName }!!
    }

    suspend fun findAll(): List<Room> {
        job.join()
        return ArrayList(RoomManager.getRoomList())
    }
}