package de.ronnyfriedland.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.ronnyfriedland.data.ImageStorage
import de.ronnyfriedland.model.Entry
import de.ronnyfriedland.model.Machine
import de.ronnyfriedland.ui.theme.Accent
import de.ronnyfriedland.ui.theme.Muted
import de.ronnyfriedland.ui.theme.Surface
import de.ronnyfriedland.ui.theme.Surface2
import de.ronnyfriedland.ui.theme.TextMain
import de.ronnyfriedland.ui.util.fmt
import de.ronnyfriedland.ui.util.localized

/** Grid item displaying machine photo/initials, name, and latest training stats. */
@Composable
fun MachineTile(
    machine: Machine,
    latestEntry: Entry? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bitmap = remember(machine.image) { ImageStorage.loadImageBitmap(machine.image) }
    Card(modifier, colors = CardDefaults.cardColors(containerColor = Surface), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(12.dp)) {
            if (bitmap != null) {
                Image(
                    bitmap.asImageBitmap(),
                    contentDescription = machine.name,
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )
            } else {
                Box(
                    Modifier.fillMaxWidth().height(100.dp).background(Surface2, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(machine.name.take(1).uppercase(), color = Accent, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(machine.name, color = TextMain, fontWeight = FontWeight.SemiBold, maxLines = 1)
            if (latestEntry != null) {
                Text(
                    "${fmt(latestEntry.weight)} kg · ${latestEntry.reps} ${localized(context, "reps_unit")}",
                    color = Color(0xFF8BC6A0),
                    fontSize = 13.sp
                )
            } else {
                Text(localized(context, "no_entries"), color = Muted, fontSize = 12.sp)
            }
        }
    }
}
