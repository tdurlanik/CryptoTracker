package com.example.cryptotracker

sealed class Screen(val route: String) {
    object CryptoList : Screen("crypto_list")

    object CryptoDetail : Screen("crypto_detail/{coinId}") {
        fun createRoute(coinId: String) = "crypto_detail/$coinId"
    }
}