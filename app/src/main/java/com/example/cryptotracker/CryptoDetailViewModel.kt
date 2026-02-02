package com.example.cryptotracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CryptoDetailViewModel : ViewModel() {

    private val _graphPoints = MutableStateFlow<List<GraphPoint>>(emptyList())
    val graphPoints = _graphPoints.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _selectedPeriod = MutableStateFlow(TimePeriod.WEEK)
    val selectedPeriod = _selectedPeriod.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun fetchGraphData(coinId: String, period: TimePeriod) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedPeriod.value = period
            _errorMessage.value = null

            try {
                val response = RetrofitInstance.api.getMarketChart(
                    coinId = coinId, days = period.days
                )

                val cleanPoints = response.prices.map { item ->
                    GraphPoint(timestamp = item[0].toLong(), price = item[1])
                }

                _graphPoints.value = cleanPoints

            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "Data loading failed. Please try again.)"
            } finally {
                _isLoading.value = false
            }
        }
    }
}