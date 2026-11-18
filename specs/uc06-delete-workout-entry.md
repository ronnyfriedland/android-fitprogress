# UC06: Delete Workout Entry

## 1. Description
The user deletes an individual workout entry from the machine's history list by using a horizontal swipe gesture that reveals a red delete button with a trash icon.

---

## 2. Actors
- Mobile App User

---

## 3. Preconditions
- The user is on `DetailScreen` and at least one workout entry exists in the history list.

---

## 4. Main Flow
1. In the history list, the user swipes a workout entry card horizontally to the left (`drag < -35f`).
2. The card animates or shifts, and a red surface button (`0xFFB3261E`) with a custom vector `TrashIcon` appears on the right edge of the item row.
3. The user taps the red delete button.
4. The system executes `EntryRepository.deleteEntry(machineId, entry)`.
5. The entry is removed from the list, and the remaining list and latest entry stats are re-evaluated and updated.

---

## 5. Alternative Flows
- **Dismiss Swipe / Swipe Back:** If the user drags to the right (`drag > 35f`), `revealDelete` is set to `false`, hiding the trash button and restoring the original full-width card layout.
- **Accidental Swipe Cancellation:** If the user releases without tapping the trash icon, the delete button remains visible until swiped back or scrolled.

---

## 6. Acceptance Criteria
- [ ] Swiping left reveals the delete button with smooth drag detection.
- [ ] Swiping right hides the delete button.
- [ ] Tapping the revealed delete button deletes the selected entry immediately.
- [ ] If the deleted entry was the latest one, the "Latest Entry" card automatically updates to show the previous entry (or empty state).
