package com.example.cryptotracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CryptoViewModel : ViewModel() {

    private val _cryptoList = MutableStateFlow<List<CryptoModel>>(emptyList())
    val cryptoList = _cryptoList.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    init {
        fetchCryptos()
    }

    fun fetchCryptos() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {

                val response = RetrofitInstance.api.getCryptos()

                _cryptoList.value = response

            } catch (e: Exception) {
                _errorMessage.value = "Error occured: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}