# UC03: Delete Machine

## 1. Description
The user permanently deletes a machine from their equipment catalog. Deletion requires explicit user confirmation via an AlertDialog and cascades to erase all workout entries associated with that machine.

---

## 2. Actors
- Mobile App User

---

## 3. Preconditions
- The machine exists in the catalog and is visible on the overview grid.

---

## 4. Main Flow
1. On the overview screen, the user performs a **long-press gesture** on a machine tile.
2. The system displays a confirmation `AlertDialog`:
   - Title: `delete_machine_title` ("Delete equipment?")
   - Message: `delete_machine_message` including the equipment's name ("Do you really want to delete [Name]? All associated training data will also be deleted.").
   - Confirm button: `yes` styled in warning red (`0xFFFF6B6B`).
   - Dismiss button: `no` styled in muted color.
3. The user taps "Yes".
4. The system executes:
   - `MachineRepository.deleteMachine(machine.id)`
   - `EntryRepository.deleteEntriesForMachine(machine.id)`
5. The confirmation dialog closes, and the machine tile is immediately removed from the overview grid.

---

## 5. Alternative Flows
- **User Cancels:** The user taps "No" or taps outside the dialog. No data is modified.

---

## 6. Acceptance Criteria
- [ ] Accidental taps do not delete machines (requires long-press gesture).
- [ ] Confirmation dialog explicitly states the machine name and warns about data loss.
- [ ] Deleting a machine completely purges its entry history (`entries_{machineId}`).
- [ ] Overview list refreshes automatically upon deletion.
