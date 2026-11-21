package de.ronnyfriedland.data

import android.content.Context
import androidx.core.content.edit
import de.ronnyfriedland.model.Entry
import de.ronnyfriedland.ui.util.parseDate
import org.json.JSONArray
import org.json.JSONObject

/**
 * Repository interface for managing workout entries for machines.
 */
interface EntryRepository {
    /** Returns all workout entries for a given machine, sorted with latest first. */
    fun getEntries(machineId: Long): List<Entry>

    /** Returns the latest workout entry for a given machine, or null if no entries exist. */
    fun getLatestEntry(machineId: Long): Entry?

    /** Adds a new workout entry to the given machine, returning the updated list of entries. */
    fun addEntry(machineId: Long, entry: Entry): List<Entry>

    /** Deletes a specific workout entry from the given machine, returning the updated list. */
    fun deleteEntry(machineId: Long, entry: Entry): List<Entry>

    /** Deletes all workout entries associated with a machine (e.g. when machine is deleted). */
    fun deleteEntriesForMachine(machineId: Long)
}

/**
 * SharedPreferences-backed implementation of [EntryRepository].
 */
class SharedPreferencesEntryRepository(context: Context) : EntryRepository {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getEntries(machineId: Long): List<Entry> {
        val key = keyForMachine(machineId)
        val raw = prefs.getString(key, null)
        if (raw != null) {
            return deserializeEntries(raw)
        }

        // Migration fallback: check if legacy "machines" array contains entries for this machine
        val legacyEntries = migrateLegacyEntries(machineId)
        if (legacyEntries.isNotEmpty()) {
            saveEntries(machineId, legacyEntries)
            return legacyEntries
        }
        return emptyList()
    }

    override fun getLatestEntry(machineId: Long): Entry? {
        return getEntries(machineId).maxByOrNull { parseDate(it.date) }
    }

    override fun addEntry(machineId: Long, entry: Entry): List<Entry> {
        val current = getEntries(machineId)
        val updated = (current + entry).sortedByDescending { parseDate(it.date) }
        saveEntries(machineId, updated)
        return updated
    }

    override fun deleteEntry(machineId: Long, entry: Entry): List<Entry> {
        val current = getEntries(machineId)
        val updated = current.filterNot { it == entry }
        saveEntries(machineId, updated)
        return updated
    }

    override fun deleteEntriesForMachine(machineId: Long) {
        prefs.edit {
            remove(keyForMachine(machineId))
        }
    }

    private fun saveEntries(machineId: Long, entries: List<Entry>) {
        val array = JSONArray()
        entries.forEach { e ->
            array.put(JSONObject().put("date", e.date).put("weight", e.weight).put("reps", e.reps).put("rounds", e.rounds))
        }
        prefs.edit {
            putString(keyForMachine(machineId), array.toString())
        }
    }

    private fun deserializeEntries(raw: String): List<Entry> {
        return try {
            val array = JSONArray(raw)
            (0 until array.length()).map { i ->
                val obj = array.getJSONObject(i)
                Entry(obj.optString("date"), obj.optDouble("weight"), obj.optInt("reps"), obj.optInt("rounds", 1).coerceAtLeast(1))
            }.sortedByDescending { parseDate(it.date) }
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun migrateLegacyEntries(machineId: Long): List<Entry> {
        val legacyRaw = prefs.getString("machines", null) ?: return emptyList()
        return try {
            val array = JSONArray(legacyRaw)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                if (obj.optLong("id") == machineId) {
                    val arr = obj.optJSONArray("entries") ?: return emptyList()
                    return (0 until arr.length()).map { j ->
                        val e = arr.getJSONObject(j)
                        Entry(e.optString("date"), e.optDouble("weight"), e.optInt("reps"), e.optInt("rounds", 1).coerceAtLeast(1))
                    }.sortedByDescending { parseDate(it.date) }
                }
            }
            emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun keyForMachine(machineId: Long): String = "entries_$machineId"

    companion object {
        private const val PREFS_NAME = "fitprogress"
    }
}
