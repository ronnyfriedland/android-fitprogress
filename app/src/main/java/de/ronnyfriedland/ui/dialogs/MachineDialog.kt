package de.ronnyfriedland.ui.dialogs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import de.ronnyfriedland.data.ImageStorage
import de.ronnyfriedland.ui.theme.*
import de.ronnyfriedland.ui.util.localized

/** Dialog for adding or editing a machine name and photo (camera/gallery). */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MachineDialog(context: Context, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var imagePath by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    // Launcher for camera and gallery image chooser
    val imageChooserLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val uri = data?.data
            if (uri != null) {
                imagePath = ImageStorage.copyImageToAppStorage(context, uri)
            } else {
                @Suppress("DEPRECATION")
                val bitmap = data?.extras?.get("data") as? Bitmap
                if (bitmap != null) imagePath = ImageStorage.saveBitmapToAppStorage(context, bitmap)
            }
        }
    }

    fun launchAndroidImageChooser() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        val cameraIntent = Intent("android.media.action.IMAGE_CAPTURE")
        val chooser = Intent.createChooser(galleryIntent, null).apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))
        }
        imageChooserLauncher.launch(chooser)
    }

    val preview = remember(imagePath) { ImageStorage.loadImageBitmap(imagePath) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        title = { Text(localized(context, "add_machine"), color = TextMain) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; error = false },
                    label = { Text(localized(context, "machine_name"), color = TextMain) },
                    singleLine = true,
                    isError = error,
                    supportingText = { if (error) Text(localized(context, "please_name"), color = Color(0xFFFF8A80)) },
                    colors = readableFieldColors()
                )
                Text(localized(context, "own_image"), color = Muted)
                if (preview != null) {
                    Image(
                        preview.asImageBitmap(),
                        contentDescription = localized(context, "own_image"),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .combinedClickable(
                                onClick = { launchAndroidImageChooser() },
                                onLongClick = { launchAndroidImageChooser() }
                            )
                    )
                } else {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .background(Surface2, RoundedCornerShape(12.dp))
                            .clickable { launchAndroidImageChooser() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("＋  ${localized(context, "upload_image")}", color = TextMain)
                    }
                }
                if (imagePath.isNotBlank()) {
                    TextButton(onClick = { imagePath = "" }) {
                        Text(localized(context, "remove_image"), color = Muted)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (name.isBlank()) error = true else onSave(name.trim(), imagePath)
            }) {
                Text(localized(context, "save"), color = Color(0xFF8BC6A0))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(localized(context, "cancel"), color = Muted)
            }
        }
    )
}
