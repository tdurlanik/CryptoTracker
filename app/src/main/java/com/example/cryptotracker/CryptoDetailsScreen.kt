package com.example.cryptotracker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CryptoDetailScreen(
    coinId: String, vm: CryptoDetailViewModel = viewModel(), onBackClick: () -> Unit
) {
    val points by vm.graphPoints.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val selectedPeriod by vm.selectedPeriod.collectAsState()
    val error by vm.errorMessage.collectAsState()

    LaunchedEffect(coinId) {
        vm.fetchGraphData(coinId, TimePeriod.WEEK)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Analysis: ${coinId.uppercase()}", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1E1E),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading && points.isEmpty()) {
                CircularProgressIndicator(color = Color.Cyan)
            }

            else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = coinId.uppercase(), color = Color.White, fontSize = 28.sp)


                    Text(
                        text = "Last ${selectedPeriod.label} Price Change", color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(32.dp))


                    LineChart(points = points)

                    Spacer(modifier = Modifier.height(24.dp))


                    TimePeriodSelector(
                        selectedPeriod = selectedPeriod, onPeriodSelected = { newPeriod ->
                            vm.fetchGraphData(coinId, newPeriod)
                        })
                }
            }


            if (error != null) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    containerColor = Color.Red,
                    contentColor = Color.White
                ) {
                    Text(text = error ?: "Error occurred")
                }
            }
        }
    }
}

@Composable
fun TimePeriodSelector(
    selectedPeriod: TimePeriod, onPeriodSelected: (TimePeriod) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TimePeriod.values().forEach { period ->
            val isSelected = (period == selectedPeriod)
            val containerColor = if (isSelected) Color.Green else Color.Transparent
            val contentColor = if (isSelected) Color.Black else Color.Gray

            Button(
                onClick = { onPeriodSelected(period) }, colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor, contentColor = contentColor
                ), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(text = period.label, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LineChart(points: List<GraphPoint>) {
    if (points.isEmpty()) return

    val minPrice = points.minOf { it.price }
    val maxPrice = points.maxOf { it.price }
    val currentPrice = points.last().price

    val priceRange = (maxPrice - minPrice).takeIf { it > 0 } ?: 1.0

    fun formatPrice(price: Double): String {
        return String.format(Locale.US, "$%.2f", price)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(Color(0xFF1E1E1E))
        ) {
            val width = size.width
            val height = size.height

            val padding = 20f

            val drawingWidth = width - (padding * 2)
            val drawingHeight = height * 0.8f
            val topOffset = height * 0.1f

            val stepX = drawingWidth / (points.size - 1)

            val path = Path()
            var lastX = 0f
            var lastY = 0f

            points.forEachIndexed { index, point ->
                val x = (index * stepX) + padding
                val normalizedY = (point.price - minPrice) / priceRange
                val y = (height - topOffset) - (normalizedY * drawingHeight).toFloat()

                lastX = x
                lastY = y

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path, color = Color.Green, style = Stroke(width = 5f)
            )

            drawCircle(
                color = Color.White, radius = 8f, center = Offset(lastX, lastY)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CryptoStatItem(
                label = "Lowest",
                value = formatPrice(minPrice),
                color = Color.Gray,
                alignment = Alignment.Start
            )

            CryptoStatItem(
                label = "Current",
                value = formatPrice(currentPrice),
                color = Color.Green,
                alignment = Alignment.CenterHorizontally,
                isBig = true
            )

            CryptoStatItem(
                label = "Highest",
                value = formatPrice(maxPrice),
                color = Color.LightGray,
                alignment = Alignment.End
            )
        }
    }
}

@Composable
fun CryptoStatItem(
    label: String,
    value: String,
    color: Color,
    alignment: Alignment.Horizontal,
    isBig: Boolean = false
) {
    Column(horizontalAlignment = alignment) {
        Text(
            text = label, color = Color.Gray, fontSize = 12.sp
        )
        Text(
            text = value,
            color = color,
            fontSize = if (isBig) 22.sp else 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}