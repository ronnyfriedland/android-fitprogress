package de.ronnyfriedland.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * Handles copying and saving equipment images in the internal app storage.
 */
object ImageStorage {
    /** Copies an image from a content URI into internal app storage. */
    fun copyImageToAppStorage(context: Context, uri: Uri): String {
        return try {
            val file = File(context.filesDir, "machine_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output -> input.copyTo(output) }
            }
            if (file.exists() && file.length() > 0) file.absolutePath else ""
        } catch (_: Exception) {
            ""
        }
    }

    /** Compresses and saves a Bitmap into internal app storage. */
    fun saveBitmapToAppStorage(context: Context, bitmap: Bitmap): String {
        return try {
            val file = File(context.filesDir, "machine_${System.currentTimeMillis()}.jpg")
            FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 90, it) }
            file.absolutePath
        } catch (_: Exception) {
            ""
        }
    }

    /** Decodes an image file path into a Bitmap, returning null on failure. */
    fun loadImageBitmap(path: String): Bitmap? {
        if (path.isBlank()) return null
        return try {
            BitmapFactory.decodeFile(path)
        } catch (_: Exception) {
            null
        }
    }
}
