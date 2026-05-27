package com.ismail.esonvpro.ui.language

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AppLanguage(val code: String, val name: String, val nativeName: String, val flagEmoji: String)

val supportedLanguages = listOf(
    AppLanguage("tr", "Türkçe", "Türkçe", "🇹🇷"),
    AppLanguage("en", "İngilizce", "English", "🇬🇧"),
    AppLanguage("de", "Almanca", "Deutsch", "🇩🇪"),
    AppLanguage("ku", "Kürtçe", "Kurdî", "☀️"), // Placeholder for Kurdish flag, often sun or regional flag is used
    AppLanguage("fr", "Fransızca", "Français", "🇫🇷"),
    AppLanguage("ar", "Arapça", "العربية", "🇪🇬"),
    AppLanguage("es", "İspanyolca", "Español", "🇪🇸"),
    AppLanguage("az", "Azerbaycan Türkçesi", "Azərbaycan", "🇦🇿"),
    AppLanguage("zh", "Çince", "中文", "🇨🇳"),
    AppLanguage("ko", "Korece", "한국어", "🇰🇷"),
    AppLanguage("hi", "Hintçe", "हिन्दी", "🇮🇳"),
    AppLanguage("ur", "Urduca", "اردو", "🇵🇰"),
    AppLanguage("id", "Endonezce", "Bahasa Indonesia", "🇮🇩")
)

@Composable
fun LanguageScreen(
    onLanguageSelected: (String) -> Unit
) {
    val darkGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0A1332),
            Color(0xFF140D36)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Text(
                text = "Uygulama Dili",
                color = Color(0xFFCBA153),
                fontSize = 32.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp, top = 24.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(supportedLanguages) { lang ->
                    LanguageItem(language = lang, onClick = { onLanguageSelected(lang.code) })
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun LanguageItem(language: AppLanguage, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Flag Box (Glassy Circle)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(Color.White.copy(alpha = 0.5f), Color.Transparent)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = language.flagEmoji,
                    fontSize = 24.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = language.nativeName,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
