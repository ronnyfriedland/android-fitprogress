# UC09: Data Persistence & Repository Architecture

## 1. Description
The persistence layer manages the storage and retrieval of exercise machines and workout records using Android's private SharedPreferences with structured JSON serialization, clean interface abstraction, and seamless migration from legacy monolithic storage formats.

---

## 2. Architecture & Contracts

### 2.1 MachineRepository
Interface defining operations strictly for the `Machine` domain:
```kotlin
interface MachineRepository {
    fun getMachines(): List<Machine>
    fun getMachine(id: Long): Machine?
    fun addMachine(name: String, image: String): List<Machine>
    fun deleteMachine(id: Long): List<Machine>
}
```
- Implementation: `SharedPreferencesMachineRepository`
- Storage Key: `"machines"` in private preference file `"fitprogress"`
- Serialized JSON Structure: `[{"id": 1700000000000, "name": "Bench Press", "image": "path/to/img.jpg"}]`

### 2.2 EntryRepository
Interface defining operations strictly for workout records per machine:
```kotlin
interface EntryRepository {
    fun getEntries(machineId: Long): List<Entry>
    fun getLatestEntry(machineId: Long): Entry?
    fun addEntry(machineId: Long, entry: Entry): List<Entry>
    fun deleteEntry(machineId: Long, entry: Entry): List<Entry>
    fun deleteEntriesForMachine(machineId: Long)
}
```
- Implementation: `SharedPreferencesEntryRepository`
- Storage Key: `"entries_{machineId}"` in private preference file `"fitprogress"`
- Serialized JSON Structure: `[{"date": "14.11.2026", "weight": 82.5, "reps": 10}]`

---

## 3. Migration Strategy
To guarantee zero data loss when upgrading from older monolithic formats where entries were nested inside machine objects:
1. When `getEntries(machineId)` is called:
   - If `"entries_{machineId}"` exists, parses and returns it.
   - If not found, inspects legacy `"machines"` array for the corresponding `machineId`.
   - If legacy entries exist, transparently saves them to `"entries_{machineId}"` and returns them.
2. Subsequent writes only update the isolated `"entries_{machineId}"` key.

---

## 4. Acceptance Criteria
- [ ] Complete separation of machine and entry storage keys.
- [ ] SharedPreferences operations run on private mode (`Context.MODE_PRIVATE`).
- [ ] Any legacy nested entries are seamlessly migrated on first read.
- [ ] Deleting a machine purges its specific `"entries_{machineId}"` key.
