package com.deslomator.tagtimer.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val VeryLightGray = Color(0xFFE0E0E0)
val SoftGreen = Color(0xff04a48f)

val OnDarkBackground = Color(0xFFF3ECFF)
val OnLightBackground = Color(0xFF26242C)

val colorPickerColors: List<Color> = listOf(
    Color(0xffd21212),
    Color(0xff0aa00a),
    Color(0xff368cf4),
    Color(0xffff8707),
    Color(0xffff43b3),
    Color(0xffacb303),
    Color(0xffaa4400),
    Color(0xff892ca0),
    Color(0xff888888),
    Color(0xff000000),
    Color(0xff003366),
    Color(0xffffffff),
    Color(0xFFCB2F54),
    Color(0xFF95122E),
    Color(0xFF26637E),
    Color(0xff7067cf),
    Color(0xff001582),
    Color(0xff04a48f),
    Color(0xFFEBEE3C),
    Color(0xff582417),
    Color(0xff005016),
    Color(0xff870058),
    Color(0xff272727),
    Color(0xff5ccb5f),
    Color(0xffD39F26),
    Color(0xffFE654F),
    Color(0xff334AFF),
    Color(0xffF6AE2D)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBarColors() = TopAppBarDefaults.topAppBarColors(
    containerColor = MaterialTheme.colorScheme.background,
    navigationIconContentColor = MaterialTheme.colorScheme.primary,
    titleContentColor = MaterialTheme.colorScheme.primary,
    actionIconContentColor = MaterialTheme.colorScheme.primary,
)

fun Color.brightness(): Float {
    return (0.21f * this.red) + (0.72f * this.green) + (0.07f * this.blue)
}

/*
hue from RGB formula
if (R >= G >= B) { 60 * ((G - B) / (R - B)) }
else if (G > R >= B ) { 60 * (2 - (R - B) / (G - B)) }
else if (G >= B > R ) { 60 * (2 + (B - R) / (G - R)) }
else if (B > G > R  ) { 60 * (4 - (G - R) / (B - R)) }
else if (B > R >= G ) { 60 * (4 + (R - G) / (B - G)) }
else (R >= B > G ) { 60 * (6 - (B - G) / (R - G) }
*/

fun Color.hue(): Float {
    val r = this.red
    val g = this.green
    val b = this.blue
    return if (g in b..r) { 60 * ((g - b) / (r - b)) }
    else if (g > r && r >= b) { 60 * (2 - (r - b) / (g - b)) }
//    else if (G >= B && B > R ) { 60 * (2 + (B - R) / (G - R)) } B > R is true at this point
    else if (g >= b) { 60 * (2 + (b - r) / (g - r)) }
//    else if (B > G && G > R) { 60 * (4 - (G - R) / (B - R)) } B > G is true at this point
    else if (g > r) { 60 * (4 - (g - r) / (b - r)) }
//    else if (B > R && R >= G) { 60 * (4 + (R - G) / (B - G)) } R >= G is true at this point
    else if (b > r) { 60 * (4 + (r - g) / (b - g)) }
    else { 60 * (6 - (b - g) / (r - g)) }
}

fun Color.contrasted() = if (this.brightness() > LIGHT_COLOR_THRESHOLD) OnLightBackground
else OnDarkBackground

private const val LIGHT_COLOR_THRESHOLD = 0.6f

fun Color.toHex(): String {
    val a = (this.alpha * 255).toInt()
    val r = (this.red * 255).toInt()
    val g = (this.green * 255).toInt()
    val b = (this.blue * 255).toInt()
    return String.format("%02x%02x%02x%02x", a, r, g, b).uppercase()
}



@Preview
@Composable
fun PickerColors() {
    Column {
        colorPickerColors.forEach {
            Box(
                modifier = Modifier
                    .background(it)
                    .width(150.dp)
                    .padding(6.dp)
            ) {
                Text(
                    text = it.toHex(),
                    color = it.contrasted(),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun Colors() {
    val colors = listOf(
        "background" to MaterialTheme.colorScheme.background,
        "error" to MaterialTheme.colorScheme.error,
        "errorContainer" to MaterialTheme.colorScheme.errorContainer,
        "inverseOnSurface" to MaterialTheme.colorScheme.inverseOnSurface,
        "inversePrimary" to MaterialTheme.colorScheme.inversePrimary,
        "inverseSurface" to MaterialTheme.colorScheme.inverseSurface,
        "onBackground" to MaterialTheme.colorScheme.onBackground,
        "onError" to MaterialTheme.colorScheme.onError,
        "onErrorContainer" to MaterialTheme.colorScheme.onErrorContainer,
        "onPrimary" to MaterialTheme.colorScheme.onPrimary,
        "onPrimaryContainer" to MaterialTheme.colorScheme.onPrimaryContainer,
        "onSecondary" to MaterialTheme.colorScheme.onSecondary,
        "onSecondaryContainer" to MaterialTheme.colorScheme.onSecondaryContainer,
        "onSurface" to MaterialTheme.colorScheme.onSurface,
        "onSurfaceVariant" to MaterialTheme.colorScheme.onSurfaceVariant,
        "onTertiary" to MaterialTheme.colorScheme.onTertiary,
        "onTertiaryContainer" to MaterialTheme.colorScheme.onTertiaryContainer,
        "outline" to MaterialTheme.colorScheme.outline,
        "outlineVariant" to MaterialTheme.colorScheme.outlineVariant,
        "primary" to MaterialTheme.colorScheme.primary,
        "primaryContainer" to MaterialTheme.colorScheme.primaryContainer,
        "scrim" to MaterialTheme.colorScheme.scrim,
        "secondary" to MaterialTheme.colorScheme.secondary,
        "secondaryContainer" to MaterialTheme.colorScheme.secondaryContainer,
        "surface" to MaterialTheme.colorScheme.surface,
        "surfaceTint" to MaterialTheme.colorScheme.surfaceTint,
        "surfaceVariant" to MaterialTheme.colorScheme.surfaceVariant,
        "tertiary" to MaterialTheme.colorScheme.tertiary,
        "tertiaryContainer" to MaterialTheme.colorScheme.tertiaryContainer,
    ).sortedBy { it.first }
    Column {
        colors.forEach {
            Box(
                modifier = Modifier
                    .background(it.second)
                    .width(150.dp)
            ) {
                Text(
                    text = it.first,
                    color = it.second.contrasted(),
                    fontSize = 13.sp
                )
            }
        }
    }
}



val md_theme_light_primary = Color(0xFF056D37)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFF9CF6B1)
val md_theme_light_onPrimaryContainer = Color(0xFF00210C)
val md_theme_light_secondary = Color(0xFF506352)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFDCF0DC)
val md_theme_light_onSecondaryContainer = Color(0xFF0E1F12)
val md_theme_light_tertiary = Color(0xFF2D4E55)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFEBFBFF)
val md_theme_light_onTertiaryContainer = Color(0xFF001F25)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFAFDEE)
val md_theme_light_onBackground = Color(0xFF191C19)
val md_theme_light_surface = Color(0xFFFCFDF7)
val md_theme_light_onSurface = Color(0xFF191C19)
val md_theme_light_surfaceVariant = Color(0xFFDDE5DA)
val md_theme_light_onSurfaceVariant = Color(0xFF414941)
val md_theme_light_outline = Color(0xFF717971)
val md_theme_light_inverseOnSurface = Color(0xFFF0F1EC)
val md_theme_light_inverseSurface = Color(0xFF2E312E)
val md_theme_light_inversePrimary = Color(0xFF81D997)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF056D37)
val md_theme_light_outlineVariant = Color(0xFFC1C9BF)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_dark_primary = Color(0xFF81D997)
val md_theme_dark_onPrimary = Color(0xFF003919)
val md_theme_dark_primaryContainer = Color(0xFF005227)
val md_theme_dark_onPrimaryContainer = Color(0xFF9CF6B1)
val md_theme_dark_secondary = Color(0xFFB7CCB7)
val md_theme_dark_onSecondary = Color(0xFF233426)
val md_theme_dark_secondaryContainer = Color(0xFF394B3B)
val md_theme_dark_onSecondaryContainer = Color(0xFFD3E8D3)
val md_theme_dark_tertiary = Color(0xFFA2CED9)
val md_theme_dark_onTertiary = Color(0xFF01363F)
val md_theme_dark_tertiaryContainer = Color(0xFF204D56)
val md_theme_dark_onTertiaryContainer = Color(0xFFBDEAF5)
val md_theme_dark_error = Color(0xFFFFB4AB)
val md_theme_dark_errorContainer = Color(0xFF93000A)
val md_theme_dark_onError = Color(0xFF690005)
val md_theme_dark_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_dark_background = Color(0xFF191C19)
val md_theme_dark_onBackground = Color(0xFFE2E3DE)
val md_theme_dark_surface = Color(0xFF191C19)
val md_theme_dark_onSurface = Color(0xFFE2E3DE)
val md_theme_dark_surfaceVariant = Color(0xFF414941)
val md_theme_dark_onSurfaceVariant = Color(0xFFC1C9BF)
val md_theme_dark_outline = Color(0xFF8B938A)
val md_theme_dark_inverseOnSurface = Color(0xFF191C19)
val md_theme_dark_inverseSurface = Color(0xFFE2E3DE)
val md_theme_dark_inversePrimary = Color(0xFF056D37)
val md_theme_dark_shadow = Color(0xFF000000)
val md_theme_dark_surfaceTint = Color(0xFF81D997)
val md_theme_dark_outlineVariant = Color(0xFF414941)
val md_theme_dark_scrim = Color(0xFF000000)

val seed = Color(0xFF076D37)


@Preview
@Composable
fun Colors2() {
    val colorsLight = listOf(
        "md_theme_light_primary" to md_theme_light_primary,
        "md_theme_light_onPrimary" to md_theme_light_onPrimary,
        "md_theme_light_primaryContainer" to md_theme_light_primaryContainer,
        "md_theme_light_onPrimaryContainer" to md_theme_light_onPrimaryContainer,
        "md_theme_light_secondary" to md_theme_light_secondary,
        "md_theme_light_onSecondary" to md_theme_light_onSecondary,
        "md_theme_light_secondaryContainer" to md_theme_light_secondaryContainer,
        "md_theme_light_onSecondaryContainer" to md_theme_light_onSecondaryContainer,
        "md_theme_light_tertiary" to md_theme_light_tertiary,
        "md_theme_light_onTertiary" to md_theme_light_onTertiary,
        "md_theme_light_tertiaryContainer" to md_theme_light_tertiaryContainer,
        "md_theme_light_onTertiaryContainer" to md_theme_light_onTertiaryContainer,
        "md_theme_light_error" to md_theme_light_error,
        "md_theme_light_errorContainer" to md_theme_light_errorContainer,
        "md_theme_light_onError" to md_theme_light_onError,
        "md_theme_light_onErrorContainer" to md_theme_light_onErrorContainer,
        "md_theme_light_background" to md_theme_light_background,
        "md_theme_light_onBackground" to md_theme_light_onBackground,
        "md_theme_light_surface" to md_theme_light_surface,
        "md_theme_light_onSurface" to md_theme_light_onSurface,
        "md_theme_light_surfaceVariant" to md_theme_light_surfaceVariant,
        "md_theme_light_onSurfaceVariant" to md_theme_light_onSurfaceVariant,
        "md_theme_light_outline" to md_theme_light_outline,
        "md_theme_light_inverseOnSurface" to md_theme_light_inverseOnSurface,
        "md_theme_light_inverseSurface" to md_theme_light_inverseSurface,
        "md_theme_light_inversePrimary" to md_theme_light_inversePrimary,
        "md_theme_light_shadow" to md_theme_light_shadow,
        "md_theme_light_surfaceTint" to md_theme_light_surfaceTint,
        "md_theme_light_outlineVariant" to md_theme_light_outlineVariant,
        "md_theme_light_scrim" to md_theme_light_scrim,
    )
    val colorsDark = listOf(
        "md_theme_dark_primary" to md_theme_dark_primary,
        "md_theme_dark_onPrimary" to md_theme_dark_onPrimary,
        "md_theme_dark_primaryContainer" to md_theme_dark_primaryContainer,
        "md_theme_dark_onPrimaryContainer" to md_theme_dark_onPrimaryContainer,
        "md_theme_dark_secondary" to md_theme_dark_secondary,
        "md_theme_dark_onSecondary" to md_theme_dark_onSecondary,
        "md_theme_dark_secondaryContainer" to md_theme_dark_secondaryContainer,
        "md_theme_dark_onSecondaryContainer" to md_theme_dark_onSecondaryContainer,
        "md_theme_dark_tertiary" to md_theme_dark_tertiary,
        "md_theme_dark_onTertiary" to md_theme_dark_onTertiary,
        "md_theme_dark_tertiaryContainer" to md_theme_dark_tertiaryContainer,
        "md_theme_dark_onTertiaryContainer" to md_theme_dark_onTertiaryContainer,
        "md_theme_dark_error" to md_theme_dark_error,
        "md_theme_dark_errorContainer" to md_theme_dark_errorContainer,
        "md_theme_dark_onError" to md_theme_dark_onError,
        "md_theme_dark_onErrorContainer" to md_theme_dark_onErrorContainer,
        "md_theme_dark_background" to md_theme_dark_background,
        "md_theme_dark_onBackground" to md_theme_dark_onBackground,
        "md_theme_dark_surface" to md_theme_dark_surface,
        "md_theme_dark_onSurface" to md_theme_dark_onSurface,
        "md_theme_dark_surfaceVariant" to md_theme_dark_surfaceVariant,
        "md_theme_dark_onSurfaceVariant" to md_theme_dark_onSurfaceVariant,
        "md_theme_dark_outline" to md_theme_dark_outline,
        "md_theme_dark_inverseOnSurface" to md_theme_dark_inverseOnSurface,
        "md_theme_dark_inverseSurface" to md_theme_dark_inverseSurface,
        "md_theme_dark_inversePrimary" to md_theme_dark_inversePrimary,
        "md_theme_dark_shadow" to md_theme_dark_shadow,
        "md_theme_dark_surfaceTint" to md_theme_dark_surfaceTint,
        "md_theme_dark_outlineVariant" to md_theme_dark_outlineVariant,
        "md_theme_dark_scrim" to md_theme_dark_scrim,
    )
    Column {
        colorsLight.forEach {
            Box(
                modifier = Modifier
                    .background(it.second)
                    .width(150.dp)
                    .padding(3.dp)
            ) {
                Text(
                    text = it.first,
                    color = it.second.contrasted(),
                    fontSize = 8.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        colorsDark.forEach {
            Box(
                modifier = Modifier
                    .background(it.second)
                    .width(150.dp)
                    .padding(3.dp)
            ) {
                Text(
                    text = it.first,
                    color = it.second.contrasted(),
                    fontSize = 8.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
            }
        }
    }
}