package com.ismail.esonvpro.ui.main

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val countdown by viewModel.countdownText.collectAsState()
    val todayTimes by viewModel.todayPrayerTime.collectAsState()

    // Mock determining background based on time (Aksam -> Dark, else Light)
    val isNight = System.currentTimeMillis() > (todayTimes?.aksam ?: Long.MAX_VALUE)
    
    Crossfade(
        targetState = isNight,
        animationSpec = tween(durationMillis = 1500),
        label = "Background Crossfade"
    ) { nightMode ->
        val bgColor = if (nightMode) Color(0xFF1E1E2C) else Color(0xFF87CEEB)
        val textColor = if (nightMode) Color.White else Color.Black

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Circular Progress Indicator (Canvas)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(250.dp)
                ) {
                    Canvas(modifier = Modifier.size(200.dp)) {
                        // Background track
                        drawArc(
                            color = textColor.copy(alpha = 0.2f),
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                        )
                        // Mock progress sweep (can be calculated dynamically)
                        drawArc(
                            color = Color(0xFF4CAF50), // Green for active
                            startAngle = -90f,
                            sweepAngle = 270f, // Example: 75% passed
                            useCenter = false,
                            style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // Precise Countdown Text
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Sonraki Vakte",
                            color = textColor.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = countdown,
                            color = textColor,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Vakit Listesi
                todayTimes?.let { times ->
                    val prayerList = listOf(
                        "İmsak" to formatTime(times.imsak),
                        "Güneş" to formatTime(times.gunes),
                        "Öğle" to formatTime(times.ogle),
                        "İkindi" to formatTime(times.ikindi),
                        "Akşam" to formatTime(times.aksam),
                        "Yatsı" to formatTime(times.yatsi)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                    ) {
                        prayerList.forEach { (name, time) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = name, color = textColor, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                                Text(text = time, color = textColor, fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Helper to format timestamp to HH:mm string
private fun formatTime(millis: Long): String {
    val formatter = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(millis))
}
