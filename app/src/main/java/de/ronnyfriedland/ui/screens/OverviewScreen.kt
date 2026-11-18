package de.ronnyfriedland.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.ronnyfriedland.model.Entry
import de.ronnyfriedland.model.Machine
import de.ronnyfriedland.ui.components.MachineTile
import de.ronnyfriedland.ui.theme.Accent
import de.ronnyfriedland.ui.theme.Bg
import de.ronnyfriedland.ui.theme.Muted
import de.ronnyfriedland.ui.theme.TextMain
import de.ronnyfriedland.ui.util.localized
import java.util.Locale

/** Main screen showing the list of equipment machines and floating action button. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverviewScreen(
    machines: List<Machine>,
    getLatestEntry: (Long) -> Entry?,
    onOpen: (Long) -> Unit,
    onAdd: () -> Unit,
    onLongPressMachine: (Machine) -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        containerColor = Bg,
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd, containerColor = Accent, contentColor = Color.White) {
                Text("+", fontSize = 30.sp)
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(24.dp))
            Text("FITPROGRESS", color = Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text(localized(context, "your_training"), color = TextMain, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Text(
                String.format(Locale.getDefault(), localized(context, "machines_count"), machines.size),
                color = Muted,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(22.dp))
            if (machines.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(localized(context, "no_machines"), color = TextMain, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        Text(localized(context, "add_first"), color = Muted)
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(bottom = 96.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(machines.size) { index ->
                        val machine = machines[index]
                        MachineTile(
                            machine = machine,
                            latestEntry = getLatestEntry(machine.id),
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = { onOpen(machine.id) },
                                    onLongClick = { onLongPressMachine(machine) }
                                )
                        )
                    }
                }
            }
        }
    }
}
