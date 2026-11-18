# UC05: Add Workout Entry

## 1. Description
The user logs a completed exercise set on the currently selected machine, recording the weight in kilograms and the number of repetitions. Incomplete or invalid fields are visually highlighted with red borders and localized error labels, identical to the machine creation dialog.

---

## 2. Actors
- Mobile App User

---

## 3. Preconditions
- The user is currently viewing the machine's `DetailScreen`.

---

## 4. Main Flow
1. The user taps the `+` Floating Action Button on the detail screen.
2. The system opens `EntryDialog` with:
   - Title: `entry_title` ("Training entry").
   - Static date line: Prefilled with current system date in `dd.MM.yyyy` format.
   - Outlined text field for **Weight (kg)** with decimal numeric keyboard.
   - Outlined text field for **Repetitions** with integer numeric keyboard.
   - Cancel and Save buttons.
3. The user inputs weight (supports both comma `,` and period `.` decimal separators) and integer repetitions.
4. The user taps "Save".
5. The system validates:
   - Weight is a valid positive non-negative number (`>= 0`).
   - Repetitions is a valid positive non-negative integer (`>= 0`).
6. If any field is invalid or missing:
   - Sets `isError = true` on the invalid field(s).
   - Displays supporting error text in warning color (`0xFFFF8A80`):
     - For weight: `please_weight` ("Please enter a valid weight.").
     - For repetitions: `please_reps` ("Please enter repetitions.").
   - Typing into an error-flagged field clears the error state immediately.
   - The dialog remains open.
7. If both fields are valid:
   - System calls `EntryRepository.addEntry(machineId, Entry(date, weight, reps))`.
   - Dialog closes.
   - Detail screen history list, latest stats card, and chart instantly update.

---

## 5. Alternative Flows
- **User Cancels:** Tapping "Cancel" or outside the dialog dismisses it without modifications.

---

## 6. Acceptance Criteria
- [ ] Date is automatically populated with the current date.
- [ ] Both German comma (`75,5`) and standard dot (`75.5`) decimal formats are correctly parsed.
- [ ] Empty or invalid fields are highlighted with red outlines and clear localized supporting error text.
- [ ] Error highlighting matches the visual style and behavior of `MachineDialog`.
- [ ] Entries are persisted immediately to local storage via `EntryRepository`.
- [ ] New entries are automatically sorted into the chronological position in the history list.
