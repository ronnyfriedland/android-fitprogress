package de.ronnyfriedland.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import de.ronnyfriedland.ui.theme.Muted
import de.ronnyfriedland.ui.theme.TextMain

/** Metric display card showing a small label and bold primary value. */
@Composable
fun Metric(label: String, value: String) {
    Column {
        Text(label, color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(value, color = TextMain, fontSize = 23.sp, fontWeight = FontWeight.Bold)
    }
}

/** Custom vector trash icon drawn on Canvas. */
@Composable
fun TrashIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val left = w * 0.24f
        val right = w * 0.76f
        val top = h * 0.28f
        val bottom = h * 0.88f
        drawLine(color, Offset(w * 0.16f, h * 0.20f), Offset(w * 0.84f, h * 0.20f), strokeWidth = w * 0.075f)
        drawLine(color, Offset(w * 0.38f, h * 0.10f), Offset(w * 0.62f, h * 0.10f), strokeWidth = w * 0.075f)
        drawRoundRect(color, Offset(left, top), Size(right - left, bottom - top), CornerRadius(w * 0.06f), style = Stroke(width = w * 0.07f))
        drawLine(color, Offset(w * 0.42f, h * 0.38f), Offset(w * 0.42f, h * 0.76f), strokeWidth = w * 0.055f)
        drawLine(color, Offset(w * 0.58f, h * 0.38f), Offset(w * 0.58f, h * 0.76f), strokeWidth = w * 0.055f)
    }
}
