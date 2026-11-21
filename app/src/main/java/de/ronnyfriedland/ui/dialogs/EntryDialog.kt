package de.ronnyfriedland.ui.dialogs

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import de.ronnyfriedland.ui.theme.Muted
import de.ronnyfriedland.ui.theme.Surface
import de.ronnyfriedland.ui.theme.TextMain
import de.ronnyfriedland.ui.theme.readableFieldColors
import de.ronnyfriedland.ui.util.localized
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Dialog for recording a new training entry (weight in kg and repetitions). */
@Composable
fun EntryDialog(context: Context, onDismiss: () -> Unit, onSave: (Double, Int, Int) -> Unit) {
    var weight by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var rounds by remember { mutableStateOf("") }
    var weightError by remember { mutableStateOf(false) }
    var repsError by remember { mutableStateOf(false) }
    var roundsError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        title = { Text(localized(context, "entry_title"), color = TextMain) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "${localized(context, "date")}: ${SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())}",
                    color = Muted
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = {
                        weight = it
                        weightError = false
                    },
                    label = { Text(localized(context, "weight_kg"), color = TextMain) },
                    singleLine = true,
                    isError = weightError,
                    supportingText = {
                        if (weightError) {
                            Text(localized(context, "please_weight"), color = Color(0xFFFF8A80))
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = readableFieldColors()
                )
                OutlinedTextField(
                    value = reps,
                    onValueChange = {
                        reps = it
                        repsError = false
                    },
                    label = { Text(localized(context, "reps"), color = TextMain) },
                    singleLine = true,
                    isError = repsError,
                    supportingText = {
                        if (repsError) {
                            Text(localized(context, "please_reps"), color = Color(0xFFFF8A80))
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = readableFieldColors()
                )
                OutlinedTextField(
                    value = rounds,
                    onValueChange = {
                        rounds = it
                        roundsError = false
                    },
                    label = { Text(localized(context, "rounds"), color = TextMain) },
                    singleLine = true,
                    isError = roundsError,
                    supportingText = {
                        if (roundsError) {
                            Text(localized(context, "please_rounds"), color = Color(0xFFFF8A80))
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = readableFieldColors()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val w = weight.replace(',', '.').toDoubleOrNull()
                val r = reps.toIntOrNull()
                val ro = rounds.toIntOrNull()
                val isWeightValid = w != null && w >= 0
                val isRepsValid = r != null && r >= 0
                val isRoundsValid = rounds.isBlank() || (ro != null && ro >= 1)

                weightError = !isWeightValid
                repsError = !isRepsValid
                roundsError = !isRoundsValid

                if (w != null && w >= 0 && r != null && r >= 0 && isRoundsValid) {
                    onSave(w, r, (ro ?: 1).coerceAtLeast(1))
                }
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
