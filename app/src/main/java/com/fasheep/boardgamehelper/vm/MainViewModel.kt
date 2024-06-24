package com.fasheep.boardgamehelper.vm

import androidx.lifecycle.ViewModel
import com.fasheep.boardgamehelper.core.Room
import com.fasheep.boardgamehelper.data.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _aaa = MutableStateFlow<List<Room>>(emptyList())
    val roomList = _aaa.asStateFlow()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            _aaa.value = RoomRepository.findAll()
        }
    }
}