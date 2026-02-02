package com.example.cryptotracker

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoApi {

    @GET("coins/markets")
    suspend fun getCryptos(
        @Query("vs_currency") currency: String = "usd",
        @Query("ids") ids: String = "bitcoin,ethereum,ripple,cardano,solana,dogecoin",
        @Query("order") order: String = "market_cap_desc",
        @Query("sparkline") sparkline: Boolean = false
    ): List<CryptoModel>

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") coinId: String,
        @Query("vs_currency") currency: String = "usd",
        @Query("days") days: Int
    ): MarketChartResponse
}
