package com.fasheep.boardgamehelper.ui

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.fasheep.boardgamehelper.R
import com.fasheep.boardgamehelper.android.MatrixToBitmapConvertor
import com.fasheep.boardgamehelper.core.Room
import com.google.zxing.WriterException

@Composable
fun QRCodeScreen(room: Room) {
    val context = LocalContext.current
    var currentPlayer by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    val secretTextList = remember {
        room.rearrange()
        room.getTargetText() ?: emptyList()
    }
    val bitmapList = remember {
        room.let {
            try {
                val matrixList = it.matrixList
                val convertor = MatrixToBitmapConvertor()
                matrixList.map { matrix -> convertor.convert(matrix) }
            } catch (e: WriterException) {
                e.printStackTrace()
                Toast.makeText(context, R.string.err_text1, Toast.LENGTH_LONG).show()
                emptyList<Bitmap>()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = context.getString(R.string.tips_text2, currentPlayer + 1),
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Image(
            bitmap = bitmapList.getOrNull(currentPlayer)?.asImageBitmap()
                ?: Bitmap.createBitmap(
                    1,
                    1,
                    Bitmap.Config.ARGB_8888
                ).asImageBitmap(),
            contentDescription = "QR Code",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .clickable { showDialog = true },
            contentScale = ContentScale.Fit
        )

        Row(
            modifier = Modifier
                .padding(bottom = 30.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = { currentPlayer = (currentPlayer - 1).coerceAtLeast(0) },
                modifier = Modifier.weight(1f),
                enabled = currentPlayer > 0
            ) {
                Text(text = context.getString(R.string.previous))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    currentPlayer =
                        (currentPlayer + 1).coerceAtMost(bitmapList.size - 1)
                },
                modifier = Modifier.weight(1f),
                enabled = currentPlayer < bitmapList.size - 1
            ) {
                Text(text = context.getString(R.string.next))
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(context.getString(R.string.dialog_result_text, currentPlayer + 1)) },
                text = { Text(secretTextList.getOrNull(currentPlayer) ?: "") },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text(context.getString(R.string.confirm))
                    }
                }
            )
        }
    }
}