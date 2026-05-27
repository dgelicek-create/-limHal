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
import com.ismail.esonvpro.data.remote.KtorClient
import com.ismail.esonvpro.data.repository.PrayerTimeRepository
import com.ismail.esonvpro.sensor.QiblaSensorManager
import com.ismail.esonvpro.ui.language.LanguageScreen
import com.ismail.esonvpro.ui.main.MainScreen
import com.ismail.esonvpro.ui.main.MainViewModel
import com.ismail.esonvpro.ui.qibla.QiblaScreen
import com.ismail.esonvpro.ui.welcome.WelcomeScreen
import java.util.Locale

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
    var currentLanguage by remember { mutableStateOf("tr") }
    val context = androidx.compose.ui.platform.LocalContext.current

    // Update Locale when currentLanguage changes
    LaunchedEffect(currentLanguage) {
        val locale = Locale(currentLanguage)
        Locale.setDefault(locale)
        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    if (currentScreen == "welcome") {
        WelcomeScreen(
            onStartClicked = { currentScreen = "home" },
            onLanguageClicked = { currentScreen = "language" }
        )
    } else if (currentScreen == "language") {
        LanguageScreen(onLanguageSelected = { langCode ->
            currentLanguage = langCode
            currentScreen = "welcome"
        })
    } else {
        Scaffold(
            bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Text("⏰") },
                    label = { Text(androidx.compose.ui.res.stringResource(id = com.ismail.esonvpro.R.string.nav_times)) },
                    selected = currentScreen == "home",
                    onClick = { currentScreen = "home" }
                )
                NavigationBarItem(
                    icon = { Text("🧭") },
                    label = { Text(androidx.compose.ui.res.stringResource(id = com.ismail.esonvpro.R.string.nav_qibla)) },
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
}
