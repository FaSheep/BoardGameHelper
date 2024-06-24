package com.fasheep.boardgamehelper.vm

import androidx.lifecycle.ViewModel
import com.fasheep.boardgamehelper.core.Role
import com.fasheep.boardgamehelper.core.Room
import com.fasheep.boardgamehelper.data.OnlineQRCodeRepository
import com.fasheep.boardgamehelper.data.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RoomViewModel : ViewModel() {
    private lateinit var currentRoom: Room
    private var currentRoomName = ""
    private val _roleList = MutableStateFlow(emptyList<Role>())
    val roleList = _roleList.asStateFlow()

    val room: Room
        get() {
            val room = Room(currentRoom.roomName, currentRoom.roomImagePath, currentRoom.formatText)
            _roleList.value.forEach { room.addRole(it.name, it.number, it.imagePath) }
            return room
        }

    fun loadRoom(roomName: String, reload: Boolean = false) {
        if (currentRoomName == roomName && !reload) return
        currentRoom = RoomRepository.findRoomByName(roomName)
        val list = mutableListOf<Role>()
        list.addAll(currentRoom.roleList)
        _roleList.value = list
        currentRoomName = roomName
    }

    fun addRole(role: Role) {
        _roleList.update { _roleList.value + listOf(role) }
    }

    fun deleteRole(role: Role) {
        _roleList.update { it.toMutableList().apply { remove(role) } }
    }

    fun editNum(role: Role, number: Int) {
        val index = _roleList.value.indexOf(role)
        if (index != -1) {
            // 使用 update 函数更新 _roleList 中的 Role 对象
            val newRole = Role(role.name, role.defNumber, role.imagePath)
            newRole.number = number
            _roleList.update { it.toMutableList().apply { set(index, newRole) } }
        }
    }

    suspend fun addOnlineRoom(): Result<String> {
        return OnlineQRCodeRepository.addRoom(room)
    }

    fun cleanRoom() {
        currentRoomName = ""
        _roleList.value = emptyList()
    }
}