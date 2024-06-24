package com.fasheep.boardgamehelper.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fasheep.boardgamehelper.R
import com.fasheep.boardgamehelper.core.Role
import com.fasheep.boardgamehelper.navigation.Screen
import com.fasheep.boardgamehelper.res.Resource
import com.fasheep.boardgamehelper.vm.RoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(roomViewModel: RoomViewModel, navController: NavController) {
    val roles by roomViewModel.roleList.collectAsState()
    val context = LocalContext.current
    var isOnlineMode by remember { mutableStateOf(true) }
    val count = roles.sumOf { it.number }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = context.getString(R.string.title, count))
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(170.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(roles) { r ->
                    RoleCard(
                        r,
                        onRoleNumberChange = roomViewModel::editNum,
                        onRoleDelete = roomViewModel::deleteRole
                    )
                }
                item {
                    AddRoleCard(onAddRole = { name ->
                        roomViewModel.addRole(Role(name, 1, "defRoleImage"))
                    })
                }
            }
            Row(
                modifier = Modifier
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = isOnlineMode,
                        onCheckedChange = { isOnlineMode = it },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = context.getString(R.string.online_mode))
                }

                Button(
                    onClick = {
                        if (count > 0) {
                            navController.navigate(Screen.QRCode.createRoute(isOnlineMode))
                        } else {
                            Toast.makeText(context, R.string.toast_text, Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.submit_button))
                }
            }
        }
    }
}

@Composable
fun RoleCard(role: Role, onRoleNumberChange: (Role, Int) -> Unit, onRoleDelete: (Role) -> Unit) {
    var roleNumber by remember(role.number) { mutableIntStateOf(role.number) }
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.padding(6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(10.dp)
        ) {
            Row {
                Spacer(modifier = Modifier.size(20.dp))
                Image(
                    painter = painterResource(id = Resource.getID(role.imagePath)),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(max = 48.dp)
                )
                Icon(
                    imageVector = Icons.Filled.Close, contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onRoleDelete(role) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = role.name)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        roleNumber = (roleNumber - 1).coerceAtLeast(0)
                        onRoleNumberChange(role, roleNumber)
                    },
                    enabled = roleNumber > 0,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .height(26.dp)
                        .widthIn(16.dp, 30.dp)
                        .weight(1f)
                        .align(Alignment.Bottom)
                ) {
                    BasicTextField(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        value = roleNumber.toString(),
                        onValueChange = {
                            roleNumber = if (it.isEmpty()) {
                                0
                            } else {
                                try {
                                    it.toInt().coerceAtLeast(0)
                                } catch (e: NumberFormatException) {
                                    roleNumber
                                }
                            }
                            onRoleNumberChange(role, roleNumber)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        singleLine = true
                    )
                    HorizontalDivider(
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = {
                        roleNumber++
                        onRoleNumberChange(role, roleNumber)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.White)
                    )
                }
            }
        }
    }
}

@Composable
fun AddRoleCard(onAddRole: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var newRoleName by remember { mutableStateOf("") }
    val context = LocalContext.current

    Card(
        modifier = Modifier.padding(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { showDialog = true }
    ) {
        Column(
            modifier = Modifier
                .padding(42.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_add),
                contentDescription = null,
                modifier = Modifier.size(66.dp)
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = context.getString(R.string.add_role_text)) },
            text = {
                OutlinedTextField(
                    value = newRoleName,
                    onValueChange = { newRoleName = it },
                    label = { Text(text = "Role Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (newRoleName.isNotBlank()) {
                        onAddRole(newRoleName)
                        newRoleName = ""
                    }
                    showDialog = false
                }) {
                    Text(text = context.getString(R.string.confirm))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = context.getString(R.string.cancel))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreView() {
    Column {
        RoleCard(
            role = Role("A", 2, "defRoleImage"),
            onRoleNumberChange = { _, _ -> },
            onRoleDelete = {})
        Spacer(modifier = Modifier.height(20.dp))
        AddRoleCard {}
    }
}
