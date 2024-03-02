import android.graphics.Color

// This data class holds the RGBA components of a color
data class ColorComponents(val r: Int, val g: Int, val b: Int, val a: Int)

// Extension function to convert a hexadecimal color string to an Int representation of a color
fun String.toColor(): Int {
    val hex = this.trim { it !in '0'..'9' && it !in 'a'..'f' && it !in 'A'..'F' }
    var colorInt = Color.WHITE
    if (hex.length in listOf(6, 8)) {
        colorInt = Color.parseColor("#$hex")
    }
    return colorInt
}

// Extension function to determine if a color (Int) is bright
fun Int.isBright(): Boolean {
    val r = Color.red(this) / 255.0
    val g = Color.green(this) / 255.0
    val b = Color.blue(this) / 255.0

    // Formula for brightness
    val brightness = 0.299 * r + 0.587 * g + 0.114 * b
    return brightness > 0.5
}

// Extension function to extract RGBA components from a color (Int)
fun Int.toComponents(): ColorComponents {
    val r = Color.red(this)
    val g = Color.green(this)
    val b = Color.blue(this)
    val a = Color.alpha(this)
    return ColorComponents(r, g, b, a)
}

// Helper function to convert RGBA components to a hexadecimal color string
fun ColorComponents.toHex(): String {
    return String.format("#%02X%02X%02X%02X", a, r, g, b)
}
