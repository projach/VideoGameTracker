package com.projach.videogametracker
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.projach.videogametracker.ui.theme.VideoGameTrackerTheme
import com.projach.videogametracker.ui.videoGamesList.VideoGamesListScreen
import com.projach.videogametracker.utils.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {
            VideoGameTrackerTheme {
                val navController = rememberNavController()
                Scaffold { contentPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = VideoGamesList,
                        modifier = Modifier.padding(contentPadding)
                    ){
                        composable<VideoGamesList> {
                            Logger.d("navHost", "Navigating to video games list screen")
                            VideoGamesListScreen()
                        }
                    }
                }
            }
        }
    }
}

