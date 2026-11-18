package de.ronnyfriedland.ui

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import de.ronnyfriedland.data.EntryRepository
import de.ronnyfriedland.data.MachineRepository
import de.ronnyfriedland.data.SharedPreferencesEntryRepository
import de.ronnyfriedland.data.SharedPreferencesMachineRepository
import de.ronnyfriedland.model.Entry
import de.ronnyfriedland.model.Machine
import de.ronnyfriedland.ui.dialogs.EntryDialog
import de.ronnyfriedland.ui.dialogs.MachineDialog
import de.ronnyfriedland.ui.screens.DetailScreen
import de.ronnyfriedland.ui.screens.OverviewScreen
import de.ronnyfriedland.ui.theme.Bg
import de.ronnyfriedland.ui.theme.FitProgressTheme
import de.ronnyfriedland.ui.theme.Muted
import de.ronnyfriedland.ui.theme.Surface
import de.ronnyfriedland.ui.theme.TextMain
import de.ronnyfriedland.ui.util.localized
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Root composable managing global machine state, dialogs, and navigation. */
@Composable
fun FitProgressApp(
    context: Context,
    machineRepository: MachineRepository = remember { SharedPreferencesMachineRepository(context) },
    entryRepository: EntryRepository = remember { SharedPreferencesEntryRepository(context) }
) {
    var machines by remember { mutableStateOf(machineRepository.getMachines()) }
    var selectedId by remember { mutableStateOf<Long?>(null) }
    var entriesForSelected by remember { mutableStateOf<List<Entry>>(emptyList()) }
    var showMachineDialog by remember { mutableStateOf(false) }
    var showEntryDialog by remember { mutableStateOf(false) }
    var showChart by remember { mutableStateOf(false) }
    var machinePendingDelete by remember { mutableStateOf<Machine?>(null) }

    // Update entries whenever a machine is selected
    LaunchedEffect(selectedId) {
        entriesForSelected = selectedId?.let { entryRepository.getEntries(it) } ?: emptyList()
    }

    val selected = machines.firstOrNull { it.id == selectedId }

    FitProgressTheme {
        Surface(Modifier.fillMaxSize(), color = Bg) {
            if (selected == null) {
                OverviewScreen(
                    machines = machines,
                    getLatestEntry = { machineId -> entryRepository.getLatestEntry(machineId) },
                    onOpen = { selectedId = it },
                    onAdd = { showMachineDialog = true },
                    onLongPressMachine = { machinePendingDelete = it }
                )
            } else {
                DetailScreen(
                    machine = selected,
                    entries = entriesForSelected,
                    onBack = { selectedId = null; showChart = false },
                    onAdd = { showEntryDialog = true },
                    onChart = { showChart = !showChart },
                    showChart = showChart,
                    onDeleteEntry = { entry ->
                        entriesForSelected = entryRepository.deleteEntry(selected.id, entry)
                    }
                )
            }
        }
    }

    // Confirmation dialog for machine deletion
    machinePendingDelete?.let { machine ->
        AlertDialog(
            onDismissRequest = { machinePendingDelete = null },
            containerColor = Surface,
            title = { Text(localized(context, "delete_machine_title"), color = TextMain) },
            text = { Text(String.format(localized(context, "delete_machine_message"), machine.name), color = TextMain) },
            confirmButton = {
                TextButton(onClick = {
                    machines = machineRepository.deleteMachine(machine.id)
                    entryRepository.deleteEntriesForMachine(machine.id)
                    machinePendingDelete = null
                }) {
                    Text(localized(context, "yes"), color = Color(0xFFFF6B6B))
                }
            },
            dismissButton = {
                TextButton(onClick = { machinePendingDelete = null }) {
                    Text(localized(context, "no"), color = Muted)
                }
            }
        )
    }

    if (showMachineDialog) {
        MachineDialog(context = context, onDismiss = { showMachineDialog = false }) { name, image ->
            machines = machineRepository.addMachine(name, image)
            showMachineDialog = false
        }
    }

    if (showEntryDialog && selected != null) {
        EntryDialog(context = context, onDismiss = { showEntryDialog = false }) { weight, reps ->
            val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())
            entriesForSelected = entryRepository.addEntry(selected.id, Entry(date, weight, reps))
            showEntryDialog = false
        }
    }
}
