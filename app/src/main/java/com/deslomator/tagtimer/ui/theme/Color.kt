package com.deslomator.tagtimer.ui.theme

import androidx.compose.ui.graphics.Color

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