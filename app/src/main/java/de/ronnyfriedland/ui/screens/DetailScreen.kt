package de.ronnyfriedland.ui.screens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.ronnyfriedland.data.ImageStorage
import de.ronnyfriedland.model.Entry
import de.ronnyfriedland.model.Machine
import de.ronnyfriedland.ui.components.ChartCard
import de.ronnyfriedland.ui.components.Metric
import de.ronnyfriedland.ui.components.TrashIcon
import de.ronnyfriedland.ui.theme.*
import de.ronnyfriedland.ui.util.fmt
import de.ronnyfriedland.ui.util.localized
import de.ronnyfriedland.ui.util.parseDate

/** Detail screen for a machine showing latest entry, progress chart, and history list. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailScreen(
    machine: Machine,
    entries: List<Entry>,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onChart: () -> Unit,
    showChart: Boolean,
    onDeleteEntry: (Entry) -> Unit,
    onChangeImage: (String) -> Unit
) {
    val context = LocalContext.current
    var showImageFullscreen by remember { mutableStateOf(false) }
    val machineImage = remember(machine.image) { ImageStorage.loadImageBitmap(machine.image) }

    // Launcher for camera and gallery image chooser (long-press on machine image)
    val imageChooserLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val uri = data?.data
            val path = if (uri != null) {
                ImageStorage.copyImageToAppStorage(context, uri)
            } else {
                @Suppress("DEPRECATION")
                val bitmap = data?.extras?.get("data") as? Bitmap
                if (bitmap != null) ImageStorage.saveBitmapToAppStorage(context, bitmap) else ""
            }
            if (path.isNotBlank()) onChangeImage(path)
        }
    }

    fun launchImageChooser() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        val cameraIntent = Intent("android.media.action.IMAGE_CAPTURE")
        val chooser = Intent.createChooser(galleryIntent, null).apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        }
        imageChooserLauncher.launch(chooser)
    }

    // Android standard back gesture handling (edge swipe / predictive back)
    BackHandler(onBack = onBack)

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
            // Header with horizontal drag to allow swiping back
            Column(
                Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        var dragX = 0f
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { _, amount ->
                                dragX += amount
                            },
                            onDragEnd = {
                                if (dragX > 70f) {
                                    onBack()
                                }
                                dragX = 0f
                            },
                            onDragCancel = { dragX = 0f }
                        )
                    }
            ) {
                Spacer(Modifier.height(20.dp))
                Text(machine.name, color = TextMain, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                if (machineImage != null) {
                    Image(
                        machineImage.asImageBitmap(),
                        contentDescription = machine.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp)
                            .height(180.dp)
                            .combinedClickable(
                                onClick = { showImageFullscreen = true },
                                onLongClick = { launchImageChooser() }
                            )
                    )
                }
                val latest = entries.maxByOrNull { parseDate(it.date) }
                Card(
                    Modifier.fillMaxWidth().padding(top = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Surface),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(localized(context, "last_entry"), color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        if (latest != null) {
                            Text(latest.date, color = Muted, fontSize = 13.sp)
                            Row(Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(30.dp)) {
                                Metric(localized(context, "weight"), "${fmt(latest.weight)} kg")
                                Metric(localized(context, "repetitions"), "${latest.reps} ${localized(context, "reps_unit")} × ${latest.rounds} ${localized(context, "rounds_unit")}")
                            }
                        } else {
                            Text(localized(context, "no_training_data"), color = Muted, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
                Button(
                    onClick = onChart,
                    modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Surface2, contentColor = TextMain),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(localized(context, if (showChart) "hide_chart" else "show_chart"))
                }
                if (showChart && entries.isNotEmpty()) {
                    ChartCard(entries)
                }
            }

            Text(
                localized(context, "history"),
                color = Muted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
            )
            if (entries.isEmpty()) {
                Text(localized(context, "entries_here"), color = Muted)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(entries.sortedByDescending { parseDate(it.date) }) { entry ->
                        var revealDelete by remember(entry) { mutableStateOf(false) }
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            // Swipeable entry card: drag left to reveal delete button
                            Card(
                                Modifier
                                    .weight(1f)
                                    .pointerInput(entry) {
                                        var drag = 0f
                                        detectHorizontalDragGestures(
                                            onHorizontalDrag = { _, amount ->
                                                drag += amount
                                                if (drag < -35f) revealDelete = true
                                                if (drag > 35f) revealDelete = false
                                            },
                                            onDragEnd = { drag = 0f }
                                        )
                                    },
                                colors = CardDefaults.cardColors(containerColor = Surface),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text(entry.date, color = TextMain, fontWeight = FontWeight.SemiBold)
                                        Text("${fmt(entry.weight)} kg", color = Color(0xFF8BC6A0), fontSize = 13.sp)
                                    }
                                    Text("${entry.reps} ${localized(context, "reps_unit")} × ${entry.rounds} ${localized(context, "rounds_unit")}", color = TextMain, fontWeight = FontWeight.Bold)
                                }
                            }
                            if (revealDelete) {
                                Spacer(Modifier.width(8.dp))
                                Surface(
                                    color = Color(0xFFB3261E),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .size(width = 64.dp, height = 66.dp)
                                        .clickable { onDeleteEntry(entry) }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        TrashIcon(Modifier.size(25.dp), Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Fullscreen image modal: tap anywhere to dismiss
    if (showImageFullscreen && machineImage != null) {
        Dialog(
            onDismissRequest = { showImageFullscreen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Bg)
                    .clickable { showImageFullscreen = false },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    machineImage.asImageBitmap(),
                    contentDescription = machine.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
