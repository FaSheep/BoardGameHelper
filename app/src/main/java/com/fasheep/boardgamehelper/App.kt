package com.fasheep.boardgamehelper

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.fasheep.boardgamehelper.data.RoomRepository

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RoomRepository.lateInit(this)
    }

    // 使用 LightColorScheme 在 App 中使用较轻的颜色
    private val colorScheme = lightColorScheme(
        primary = Color(0xFF546E7A),
        secondary = Color(0xFF673AB7),
        tertiary = Color(0xFF03DAC5),
        background = Color(0xFFEEEEEE),
        surface = Color(0xFFFFFBFE),
        onPrimary = Color.White,
        onSecondary = Color(0xFFFFFFFF),
        onTertiary = Color.White,
        onBackground = Color(0xFF1C1B1F),
        onSurface = Color(0xFF1C1B1F),
    )

    // 定义 MaterialTheme
    @Composable
    fun AppTheme(content: @Composable () -> Unit) {
        MaterialTheme(colorScheme = colorScheme, content = content)
    }
}