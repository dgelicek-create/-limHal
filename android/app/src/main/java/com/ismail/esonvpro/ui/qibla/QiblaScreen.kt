package com.ismail.esonvpro.ui.qibla

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ismail.esonvpro.sensor.QiblaSensorManager

@Composable
fun QiblaScreen(qiblaSensorManager: QiblaSensorManager) {
    val qiblaAngle by qiblaSensorManager.qiblaAngle.collectAsState()

    // Smooth Compose animation for rotation just in case
    val animatedAngle by animateFloatAsState(
        targetValue = qiblaAngle,
        animationSpec = tween(durationMillis = 300),
        label = "Qibla Rotation"
    )

    DisposableEffect(Unit) {
        qiblaSensorManager.startListening()
        onDispose {
            qiblaSensorManager.stopListening()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E2C)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Kıble Pusulası",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2A2A3D))
            ) {
                // Outer Compass Ring
                Canvas(modifier = Modifier.size(280.dp)) {
                    drawCircle(
                        color = Color.Gray.copy(alpha = 0.5f),
                        style = Stroke(width = 4.dp.toPx())
                    )
                }

                // Inner Rotating Needle targeting Kaaba
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .rotate(animatedAngle),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val path = Path().apply {
                            moveTo(size.width / 2, 0f) // Top tip
                            lineTo(size.width * 0.6f, size.height / 2) // Right curve
                            lineTo(size.width / 2, size.height * 0.8f) // Bottom center
                            lineTo(size.width * 0.4f, size.height / 2) // Left curve
                            close()
                        }
                        drawPath(
                            path = path,
                            color = Color(0xFF4CAF50) // Green needle pointing to Kaaba
                        )
                    }
                }
                
                // Center pin
                Canvas(modifier = Modifier.size(16.dp)) {
                    drawCircle(color = Color.White)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Cihazı yere paralel tutun",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}
