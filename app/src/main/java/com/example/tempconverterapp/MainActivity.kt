package com.example.tempconverterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TempConverterTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TempConverterApp()
                }
            }
        }
    }
}

@Composable
fun TempConverterTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}

@Composable
fun TempConverterApp() {
    var celsius by remember { mutableFloatStateOf(0f) }
    var fahrenheit by remember { mutableFloatStateOf(32f) }
    var isFahrenheitDragging by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Temperature Converter",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TempSlider(
            value = celsius,
            onValueChange = { newCelsius ->
                celsius = newCelsius
                fahrenheit = (newCelsius * 9/5 + 32).roundToInt().toFloat()
            },
            valueRange = 0f..100f,
            label = "Celsius",
            symbol = "°C"
        )

        Spacer(modifier = Modifier.height(32.dp))

        TempSlider(
            value = fahrenheit,
            onValueChange = { newFahrenheit ->
                fahrenheit = newFahrenheit
                if (!isFahrenheitDragging) {
                    celsius = ((fahrenheit - 32) * 5/9).roundToInt().toFloat()
                }
            },
            valueRange = 0f..212f,
            label = "Fahrenheit",
            symbol = "°F",
            onDragStart = { isFahrenheitDragging = true },
            onDragEnd = {
                isFahrenheitDragging = false
                if (fahrenheit < 32f) {
                    fahrenheit = 32f
                    celsius = 0f
                } else {
                    celsius = ((fahrenheit - 32) * 5/9).roundToInt().toFloat()
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (celsius <= 20) "I wish it were warmer." else "I wish it were colder.",
            fontSize = 18.sp,
            color = if (celsius <= 20) Color.Blue else Color.Red
        )
    }
}

@Composable
fun TempSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    label: String,
    symbol: String,
    onDragStart: () -> Unit = {},
    onDragEnd: () -> Unit = {}
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$label: ${value.roundToInt()}$symbol",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier.width(300.dp),
            onValueChangeFinished = onDragEnd
        )
    }

    DisposableEffect(value) {
        onDragStart()
        onDispose { onDragEnd() }
    }
}

@Preview(showBackground = true)
@Composable
fun TempConverterAppPreview() {
    TempConverterTheme {
        TempConverterApp()
    }
}