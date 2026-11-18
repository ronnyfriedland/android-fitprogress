package de.ronnyfriedland.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.ronnyfriedland.model.Entry
import de.ronnyfriedland.ui.theme.Muted
import de.ronnyfriedland.ui.theme.Surface
import de.ronnyfriedland.ui.util.localized
import de.ronnyfriedland.ui.util.parseDate

/** Bar chart visualizing recent weight and repetition progression. */
@Composable
fun ChartCard(entries: List<Entry>) {
    val context = LocalContext.current
    val ordered = entries.sortedBy { parseDate(it.date) }.takeLast(8)
    Card(
        Modifier.fillMaxWidth().padding(top = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(localized(context, "chart_title"), color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(vertical = 8.dp)) {
                Text(localized(context, "chart_weight"), color = Color(0xFF8BC6A0), fontSize = 11.sp)
                Text(localized(context, "chart_reps"), color = Color(0xFFB4C3BC), fontSize = 11.sp)
            }
            Canvas(Modifier.fillMaxWidth().height(150.dp)) {
                val n = ordered.size
                if (n == 0) return@Canvas
                val maxW = (ordered.maxOf { it.weight }.toFloat()).coerceAtLeast(1f)
                val maxR = (ordered.maxOf { it.reps }.toFloat()).coerceAtLeast(1f)
                val group = size.width / n
                val barW = (group * 0.25f).coerceAtLeast(5f)
                ordered.forEachIndexed { i, e ->
                    val x = group * i + group * 0.25f
                    val h1 = size.height * 0.78f * (e.weight.toFloat() / maxW)
                    val h2 = size.height * 0.78f * (e.reps.toFloat() / maxR)
                    drawRect(Color(0xFF8BC6A0), Offset(x, size.height - h1), Size(barW, h1))
                    drawRect(Color(0xFFB4C3BC), Offset(x + barW + 3, size.height - h2), Size(barW, h2))
                }
            }
            Text(localized(context, "chart_scale_note"), color = Muted, fontSize = 10.sp)
        }
    }
}
