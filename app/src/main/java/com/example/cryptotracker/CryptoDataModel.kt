package com.example.cryptotracker

import com.google.gson.annotations.SerializedName

data class CryptoModel(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,

    @SerializedName("current_price")
    val currentPrice: Double,

    @SerializedName("price_change_percentage_24h")
    val priceChangePct: Double
)
data class MarketChartResponse(
    val prices: List<List<Double>>
)
data class GraphPoint(
    val timestamp: Long,
    val price: Double
)
enum class TimePeriod(val label: String, val days: Int) {
    WEEK("7 Days", 7),
    MONTH("1 Month", 30),
    SIX_MONTHS("6 Months", 180),
    YEAR("1 Year", 365)
}