package com.kkr.juken.core.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Juken Design Tokens - The Single Source of Truth for all UI constants.
 */
object JukenTokens {
    
    object Colors {
        val Primary = Color(0xFF00E5FF) // Electric Cyan
        val OnPrimary = Color(0xFF00363D)
        val PrimaryContainer = Color(0xFF004E56)
        val OnPrimaryContainer = Color(0xFF80F3FF)
        
        val Background = Color(0xFF0A0E21) // Deep Indigo
        val Surface = Color(0xFF1C1B1F)
        val GlassBase = Color(0x33FFFFFF) // 20% White
        val GlassStroke = Color(0x4DFFFFFF) // 30% White
        
        val Success = Color(0xFF00C853) // Vivid Emerald
        val Warning = Color(0xFFFFD600) // Amber
        val Critical = Color(0xFFFF1744) // Crimson Pulse
        
        val Gray400 = Color(0xFF9E9E9E)
        val Gray800 = Color(0xFF424242)
    }
    
    object Spacing {
        val None = 0.dp
        val ExtraSmall = 4.dp
        val Small = 8.dp
        val Medium = 16.dp
        val Large = 24.dp
        val ExtraLarge = 32.dp
        val Gutter = 12.dp
    }
    
    object Radius {
        val Small = 8.dp
        val Medium = 16.dp
        val Large = 28.dp
        val ExtraLarge = 40.dp
        val Full = 1000.dp
    }
    
    object Elevation {
        val Level0 = 0.dp
        val Level1 = 1.dp
        val Level2 = 3.dp
        val Level3 = 6.dp
    }
    
    object Glass {
        val BlurNormal = 20.dp
        val BlurHigh = 40.dp
        val StrokeThin = 0.5.dp
        val StrokeNormal = 1.dp
    }
    
    object Typography {
        val LabelSize = 12.sp
        val BodySize = 16.sp
        val TitleSize = 20.sp
        val DisplayLargeSize = 48.sp
        val DisplayMediumSize = 32.sp
    }
}
