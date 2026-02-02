package com.example.cryptotracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = Screen.CryptoList.route
            ) {

                composable(route = Screen.CryptoList.route) {
                    CryptoScreen(
                        onCoinClick = { coinId ->
                            navController.navigate(Screen.CryptoDetail.createRoute(coinId))
                        })
                }

                composable(route = Screen.CryptoDetail.route) { backStackEntry ->
                    val coinId = backStackEntry.arguments?.getString("coinId") ?: "unknown"

                    CryptoDetailScreen(
                        coinId = coinId, onBackClick = {
                            navController.popBackStack()
                        })
                }
            }
        }
    }
}