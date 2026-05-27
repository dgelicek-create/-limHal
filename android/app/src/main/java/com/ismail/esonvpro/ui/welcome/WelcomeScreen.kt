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
import androidx.compose.ui.res.stringResource
import com.ismail.esonvpro.R
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
fun WelcomeScreen(onStartClicked: () -> Unit, onLanguageClicked: () -> Unit) {
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
            
            // Draw intersecting dark blue circles at top left (based on user sketch)
            val stroke = Stroke(width = 2.dp.toPx())
            val strokeColor = Color(0xFF203575) // Slightly lighter than background
            
            drawCircle(color = strokeColor, radius = 50.dp.toPx(), center = Offset(40.dp.toPx(), 40.dp.toPx()), style = stroke)
            drawCircle(color = strokeColor, radius = 45.dp.toPx(), center = Offset(110.dp.toPx(), 60.dp.toPx()), style = stroke)
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
                text = stringResource(id = R.string.welcome_greeting),
                color = goldColor,
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = stringResource(id = R.string.app_name),
                color = goldColor,
                fontSize = 42.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = stringResource(id = R.string.welcome_slogan),
                color = lightGray,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Language Selector
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E0E0)) // Light gray background
                    .clickable { onLanguageClicked() }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(id = R.string.welcome_language_selector), color = Color(0xFF333333), fontSize = 16.sp)
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
                
                val termsPart1 = stringResource(id = R.string.terms_part1)
                val termsLink = stringResource(id = R.string.terms_link)
                val termsPart2 = stringResource(id = R.string.terms_part2)
                val privacyLink = stringResource(id = R.string.privacy_link)
                val termsPart3 = stringResource(id = R.string.terms_part3)
                
                val annotatedString = buildAnnotatedString {
                    append(termsPart1)
                    pushStringAnnotation(tag = "TERMS", annotation = "terms")
                    withStyle(style = SpanStyle(color = lightGray, textDecoration = TextDecoration.Underline)) {
                        append(termsLink)
                    }
                    pop()
                    append(termsPart2)
                    pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
                    withStyle(style = SpanStyle(color = lightGray, textDecoration = TextDecoration.Underline)) {
                        append(privacyLink)
                    }
                    pop()
                    append(termsPart3)
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
                    text = stringResource(id = R.string.button_start),
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
