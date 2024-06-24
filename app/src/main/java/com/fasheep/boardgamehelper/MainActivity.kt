package com.fasheep.boardgamehelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fasheep.boardgamehelper.navigation.Screen
import com.fasheep.boardgamehelper.ui.OnlineQRCodeScreen
import com.fasheep.boardgamehelper.ui.QRCodeScreen
import com.fasheep.boardgamehelper.ui.RoomListScreen
import com.fasheep.boardgamehelper.ui.RoomScreen
import com.fasheep.boardgamehelper.vm.MainViewModel
import com.fasheep.boardgamehelper.vm.RoomViewModel

class MainActivity : ComponentActivity() {
    private val viewModel = MainViewModel()
    private val roomViewModel = RoomViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Surface(color = MaterialTheme.colorScheme.background) {
                NavHost(navController = navController, startDestination = Screen.RoomList.route) {
                    composable(route = Screen.RoomList.route) {
                        RoomListScreen(viewModel = viewModel, navController = navController)
                    }
                    composable(
                        route = Screen.Room.route,
                        arguments = Screen.Room.arguments
                    ) { entry ->
                        roomViewModel.loadRoom(entry.arguments?.getString(Screen.Room.roomNameArg)!!)
                        RoomScreen(roomViewModel, navController)
                    }
                    composable(
                        route = Screen.QRCode.route,
                        arguments = Screen.QRCode.arguments
                    ) {
                        if (it.arguments?.getBoolean(Screen.QRCode.onlineArg) == true) {
                            OnlineQRCodeScreen(
                                viewModel = roomViewModel,
                                navController = navController
                            )
                        } else {
                            QRCodeScreen(room = roomViewModel.room)
                        }
                    }
                }
            }
        }
    }
}