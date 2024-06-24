package com.fasheep.boardgamehelper.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fasheep.boardgamehelper.R
import com.fasheep.boardgamehelper.android.MatrixToBitmapConvertor
import com.fasheep.boardgamehelper.core.Encoder
import com.fasheep.boardgamehelper.res.Resource
import com.fasheep.boardgamehelper.vm.RoomViewModel
import com.google.zxing.WriterException


private const val URL = Resource.URL_HK

@Composable
fun OnlineQRCodeScreen(viewModel: RoomViewModel, navController: NavController) {
    val context = LocalContext.current
    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showLoading by remember { mutableStateOf(true) }
    var connectStatus by remember { mutableStateOf<String?>(null) }
    var time by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val result = viewModel.addOnlineRoom()

        when {
            result.isSuccess -> {
                time = result.getOrDefault("")
            }

            result.isFailure -> {
                connectStatus =
                    result.exceptionOrNull()?.message ?: context.getString(R.string.code_0)
            }
        }
        showLoading = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showLoading) {
            CircularProgressIndicator()
            Text(text = context.getString(R.string.keep_connect))
        } else if (connectStatus != null) {
            Text(text = connectStatus ?: "")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text(text = "OK")
            }
        } else {
            qrCodeBitmap = generateQRCodeBitmap("$URL/web/display.do?id=$time")
            qrCodeBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                val uri = Uri.parse("$URL/web/display.do?id=$time")
                val intent = Intent(Intent.ACTION_VIEW, uri)

                Button(onClick = { context.startActivity(intent) }) {
                    Text(text = "Open in Browser")
                }
            }
        }
    }
}

@Composable
private fun generateQRCodeBitmap(content: String): Bitmap? {
    return try {
        val encoder = Encoder(Encoder.EncoderType.UTF8)
        val matrix = encoder.getMatrix(content)
        val convertor = MatrixToBitmapConvertor()
        convertor.convert(matrix)
    } catch (e: WriterException) {
        e.printStackTrace()
        Toast.makeText(LocalContext.current, R.string.err_text1, Toast.LENGTH_LONG).show()
        null
    }
}