package com.ismail.esonvpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ismail.esonvpro.data.local.AppDatabase
import com.ismail.esonvpro.ui.main.MainScreen
import com.ismail.esonvpro.ui.main.MainViewModel
import com.ismail.esonvpro.ui.qibla.QiblaScreen
import com.ismail.esonvpro.ui.welcome.WelcomeScreen
import com.ismail.esonvpro.ui.welcome.WelcomeScreen

class MainActivity : ComponentActivity() {

    private lateinit var qiblaSensorManager: QiblaSensorManager
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Dependencies
        val database = AppDatabase.getDatabase(this)
        val repository = PrayerTimeRepository(database.prayerTimeDao(), KtorClient.httpClient)
        mainViewModel = MainViewModel(repository)
        qiblaSensorManager = QiblaSensorManager(this)

        setContent {
            EsonVProApp(mainViewModel, qiblaSensorManager)
        }
    }
}

@Composable
fun EsonVProApp(mainViewModel: MainViewModel, qiblaSensorManager: QiblaSensorManager) {
    var currentScreen by remember { mutableStateOf("welcome") }

    if (currentScreen == "welcome") {
        WelcomeScreen(onStartClicked = { currentScreen = "home" })
    } else {
        Scaffold(
            bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Text("⏰") },
                    label = { Text("Vakitler") },
                    selected = currentScreen == "home",
                    onClick = { currentScreen = "home" }
                )
                NavigationBarItem(
                    icon = { Text("🧭") },
                    label = { Text("Kıble") },
                    selected = currentScreen == "qibla",
                    onClick = { currentScreen = "qibla" }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (currentScreen == "home") {
                MainScreen(viewModel = mainViewModel)
            } else {
                QiblaScreen(qiblaSensorManager = qiblaSensorManager)
            }
        }
    }
}
