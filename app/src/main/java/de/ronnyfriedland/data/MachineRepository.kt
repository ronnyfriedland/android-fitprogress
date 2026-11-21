package de.ronnyfriedland.data

import android.content.Context
import androidx.core.content.edit
import de.ronnyfriedland.model.Machine
import org.json.JSONArray
import org.json.JSONObject

/**
 * Repository interface for managing gym machines/equipment.
 */
interface MachineRepository {
    /** Returns all stored machines. */
    fun getMachines(): List<Machine>

    /** Returns a specific machine by id, or null if not found. */
    fun getMachine(id: Long): Machine?

    /** Creates and stores a new machine, returning the updated list of machines. */
    fun addMachine(name: String, image: String): List<Machine>

    /** Deletes a machine by id, returning the updated list. */
    fun deleteMachine(id: Long): List<Machine>

    /** Updates the image path of a machine, returning the updated list. */
    fun updateMachineImage(id: Long, image: String): List<Machine>
}

/**
 * SharedPreferences-backed implementation of [MachineRepository].
 */
class SharedPreferencesMachineRepository(context: Context) : MachineRepository {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun getMachines(): List<Machine> {
        val raw = prefs.getString(KEY_MACHINES, null) ?: return emptyList()
        return try {
            val array = JSONArray(raw)
            (0 until array.length()).map { i ->
                val obj = array.getJSONObject(i)
                Machine(obj.getLong("id"), obj.getString("name"), obj.optString("image", "none"))
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    override fun getMachine(id: Long): Machine? {
        return getMachines().firstOrNull { it.id == id }
    }

    override fun addMachine(name: String, image: String): List<Machine> {
        val current = getMachines()
        val newMachine = Machine(
            id = System.currentTimeMillis(),
            name = name,
            image = image
        )
        val updated = current + newMachine
        saveMachines(updated)
        return updated
    }

    override fun deleteMachine(id: Long): List<Machine> {
        val updated = getMachines().filterNot { it.id == id }
        saveMachines(updated)
        return updated
    }

    override fun updateMachineImage(id: Long, image: String): List<Machine> {
        val updated = getMachines().map { if (it.id == id) it.copy(image = image) else it }
        saveMachines(updated)
        return updated
    }

    private fun saveMachines(machines: List<Machine>) {
        val array = JSONArray()
        machines.forEach { m ->
            array.put(JSONObject().put("id", m.id).put("name", m.name).put("image", m.image))
        }
        prefs.edit {
            putString(KEY_MACHINES, array.toString())
        }
    }

    companion object {
        private const val PREFS_NAME = "fitprogress"
        private const val KEY_MACHINES = "machines"
    }
}
