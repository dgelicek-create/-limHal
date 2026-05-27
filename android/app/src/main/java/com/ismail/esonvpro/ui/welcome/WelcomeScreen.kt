package com.ismail.esonvpro.ui.welcome

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(onStartClicked: () -> Unit) {
    var isChecked by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val deepBlue = Color(0xFF0A1332)
    val goldColor = Color(0xFFCBA153)
    val lightGray = Color(0xFFE0E0E0)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(deepBlue)
    ) {
        // Decorative Top Pattern (Simulated with Canvas)
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height * 0.7f)
                quadraticBezierTo(
                    size.width / 2, size.height,
                    0f, size.height * 0.6f
                )
                close()
            }
            drawPath(path, color = Color(0xFF142459))
            
            // Draw some geometric outlines (circles, diamonds) to simulate the pattern
            val stroke = Stroke(width = 2f)
            val strokeColor = goldColor.copy(alpha = 0.3f)
            
            drawCircle(color = strokeColor, radius = 60f, center = Offset(100f, 100f), style = stroke)
            drawCircle(color = strokeColor, radius = 80f, center = Offset(300f, 150f), style = stroke)
            drawArc(color = strokeColor, startAngle = 0f, sweepAngle = 180f, useCenter = false, 
                topLeft = Offset(150f, 50f), size = Size(100f, 100f), style = stroke)
            drawLine(color = strokeColor, start = Offset(0f, 200f), end = Offset(size.width, 200f), style = stroke)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(260.dp))

            // Text section
            Text(
                text = "Selâmun Aleyküm",
                color = goldColor,
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "İlmHal",
                color = goldColor,
                fontSize = 42.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "İlim yolculuğunuzu aydınlatır",
                color = lightGray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Language Selector Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E0E0)) // Light gray background
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Türkçe", color = Color(0xFF333333), fontSize = 16.sp)
                    Text(">", color = goldColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Checkbox and Agreement Text
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = goldColor,
                        uncheckedColor = goldColor,
                        checkmarkColor = deepBlue
                    )
                )
                
                val annotatedString = buildAnnotatedString {
                    append("Uygulamanın ")
                    pushStringAnnotation(tag = "TERMS", annotation = "terms")
                    withStyle(style = SpanStyle(color = lightGray, textDecoration = TextDecoration.Underline)) {
                        append("kullanım şartlarını")
                    }
                    pop()
                    append(" ve ")
                    pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
                    withStyle(style = SpanStyle(color = lightGray, textDecoration = TextDecoration.Underline)) {
                        append("gizlilik sözleşmesini")
                    }
                    pop()
                    append(" okudum ve kabul ediyorum.")
                }
                
                ClickableText(
                    text = annotatedString,
                    style = androidx.compose.ui.text.TextStyle(color = lightGray, fontSize = 14.sp),
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                            .firstOrNull()?.let { showTermsDialog = true }
                        annotatedString.getStringAnnotations(tag = "PRIVACY", start = offset, end = offset)
                            .firstOrNull()?.let { showPrivacyDialog = true }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Başla Button
            Button(
                onClick = onStartClicked,
                enabled = isChecked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = goldColor,
                    disabledContainerColor = goldColor.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "BAŞLA",
                    color = deepBlue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showTermsDialog) {
        LegalDialog(title = "Kullanım Şartları", text = LegalTexts.TERMS_OF_USE) {
            showTermsDialog = false
        }
    }
    if (showPrivacyDialog) {
        LegalDialog(title = "Gizlilik Sözleşmesi", text = LegalTexts.PRIVACY_POLICY) {
            showPrivacyDialog = false
        }
    }
}

@Composable
fun LegalDialog(title: String, text: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(text)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat")
            }
        }
    )
}
