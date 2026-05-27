package com.ismail.esonvpro.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onGoogleLogin: () -> Unit,
    onPhoneLogin: () -> Unit,
    onFacebookLogin: () -> Unit,
    onGuestLogin: () -> Unit
) {
    val darkGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0A1332),
            Color(0xFF140D36)
        )
    )
    val goldColor = Color(0xFFCBA153)
    val lightGray = Color(0xFFE0E0E0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "İlmHal'e",
                color = lightGray,
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = "Hoşgeldiniz",
                color = goldColor,
                fontSize = 36.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Google Button
            LoginButton(
                text = "Google ile Giriş Yap",
                iconText = "G",
                backgroundColor = Color.White,
                textColor = Color.DarkGray,
                onClick = onGoogleLogin
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Button
            LoginButton(
                text = "Telefon (SMS) ile Giriş",
                iconText = "📞",
                backgroundColor = Color(0xFF2E7D32), // Dark Green
                textColor = Color.White,
                onClick = onPhoneLogin
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Facebook Button
            LoginButton(
                text = "Facebook ile Giriş Yap",
                iconText = "f",
                backgroundColor = Color(0xFF1877F2), // Facebook Blue
                textColor = Color.White,
                onClick = onFacebookLogin
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.2f))
                Text(
                    text = "veya",
                    color = lightGray,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontSize = 14.sp
                )
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.2f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Guest Button
            Text(
                text = "Misafir Olarak Devam Et",
                color = goldColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onGuestLogin() }
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LoginButton(
    text: String,
    iconText: String,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iconText,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
