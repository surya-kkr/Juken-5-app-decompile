package com.kkr.juken.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kkr.juken.core.ui.theme.JukenTheme

@Preview(name = "Glass Components", showBackground = true, backgroundColor = 0xFF0A0E21)
@Composable
fun GlassComponentsPreview() {
    JukenTheme {
        GlassBackground {
            Column(modifier = Modifier.padding(16.dp)) {
                GlassCard {
                    Column {
                        CircularGauge(value = 8500f, min = 0f, max = 16000f, label = "RPM", unit = "rpm")
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearGauge(value = 45f, min = 0f, max = 100f, label = "TPS")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                GlassButton(text = "Connect ECU", onClick = {})
                Spacer(modifier = Modifier.height(16.dp))
                GlassChip(text = "Connected")
            }
        }
    }
}
