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

/** Bar chart visualizing daily training volume (weight × reps × rounds) for recent entries. */
@Composable
fun ChartCard(entries: List<Entry>) {
    val context = LocalContext.current
    val dailyVolumes = entries
        .sortedBy { parseDate(it.date) }
        .takeLast(10)
        .groupBy { it.date }
        .map { (_, dayEntries) ->
            dayEntries.sumOf { it.weight * it.reps * it.rounds }
        }
    Card(
        Modifier.fillMaxWidth().padding(top = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(localized(context, "chart_title"), color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Canvas(Modifier.fillMaxWidth().height(150.dp)) {
                val n = dailyVolumes.size
                if (n == 0) return@Canvas
                val maxV = dailyVolumes.max().toFloat().coerceAtLeast(1f)
                val group = size.width / n
                val barW = (group * 0.5f).coerceAtLeast(5f)
                dailyVolumes.forEachIndexed { i, volume ->
                    val x = group * i + (group - barW) / 2
                    val h = size.height * 0.78f * (volume.toFloat() / maxV)
                    drawRect(Color(0xFF8BC6A0), Offset(x, size.height - h), Size(barW, h))
                }
            }
            Text(localized(context, "chart_scale_note"), color = Muted, fontSize = 10.sp)
        }
    }
}
