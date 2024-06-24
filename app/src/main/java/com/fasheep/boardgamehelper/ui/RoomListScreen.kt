package com.fasheep.boardgamehelper.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fasheep.boardgamehelper.R
import com.fasheep.boardgamehelper.core.Room
import com.fasheep.boardgamehelper.navigation.Screen
import com.fasheep.boardgamehelper.res.Resource
import com.fasheep.boardgamehelper.vm.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomListScreen(viewModel: MainViewModel, navController: NavController) {
    val roomList by viewModel.roomList.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.app_name)) })
        }) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            items(roomList) { room ->
                RoomCard(room, navController)
            }
        }
    }

}

@Composable
fun RoomCard(room: Room, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate(Screen.Room.createRoute(room.roomName))
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = Resource.getID(room.roomImagePath)),
                contentDescription = null,
                modifier = Modifier
                    .padding(20.dp, 14.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Fit
            )
            Text(text = room.roomName)
        }
    }
}