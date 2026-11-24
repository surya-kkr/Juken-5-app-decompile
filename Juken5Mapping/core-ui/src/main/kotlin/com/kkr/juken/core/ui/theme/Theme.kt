package com.kkr.juken.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

private val DarkColorScheme = darkColorScheme(
    primary = JukenTokens.Colors.Primary,
    onPrimary = JukenTokens.Colors.OnPrimary,
    primaryContainer = JukenTokens.Colors.PrimaryContainer,
    onPrimaryContainer = JukenTokens.Colors.OnPrimaryContainer,
    background = JukenTokens.Colors.Background,
    surface = JukenTokens.Colors.Surface,
    onSurface = JukenTokens.Colors.Gray400
)

private val JukenTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = JukenTokens.Typography.BodySize
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = JukenTokens.Typography.TitleSize
    )
)

private val JukenShapes = Shapes(
    small = RoundedCornerShape(JukenTokens.Radius.Small),
    medium = RoundedCornerShape(JukenTokens.Radius.Medium),
    large = RoundedCornerShape(JukenTokens.Radius.Large)
)

internal val LocalJukenTokens = staticCompositionLocalOf { JukenTokens }

@Composable
fun JukenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalJukenTokens provides JukenTokens
    ) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography = JukenTypography,
            shapes = JukenShapes
        ) {
            Surface(
                modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                content = content
            )
        }
    }
}

object JukenTheme {
    val tokens: JukenTokens
        @Composable
        @ReadOnlyComposable
        get() = LocalJukenTokens.current
}
