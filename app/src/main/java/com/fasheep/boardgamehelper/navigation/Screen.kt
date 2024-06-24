package com.fasheep.boardgamehelper.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    data object RoomList : Screen("room_list")
    data object Room : Screen("room/{roomName}") {
        const val roomNameArg = "roomName"
        val arguments = listOf(
            navArgument(roomNameArg) { type = NavType.StringType }
        )

        fun createRoute(roomName: String) = "room/$roomName"
    }

    data object QRCode : Screen("qrcode/{online}") {
        const val onlineArg = "online"
        val arguments = listOf(
            navArgument(onlineArg) { type = NavType.BoolType }
        )

        fun createRoute(online: Boolean) = "qrcode/$online"
    }
}