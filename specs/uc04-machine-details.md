# UC04: Machine Details

## 1. Description
The user views the comprehensive detail view of a selected machine. Navigation back to the overview is performed via swipe gesture (aligned with Android 17 gesture navigation standards) rather than a text link. The screen presents the equipment name, a prominent summary card of the latest workout record, a toggleable progress chart, and the chronological history of all recorded training sets.

---

## 2. Actors
- Mobile App User

---

## 3. Preconditions
- A machine is selected from the overview screen.

---

## 4. Main Flow
1. The user taps a machine tile on the overview screen.
2. The system transitions to `DetailScreen` and queries `EntryRepository.getEntries(selectedId)`.
3. The screen renders:
   - **Back Navigation (Gesture-based):** No textual link; navigation back is handled via:
     - Android system back swipe gesture (predictive back / edge swipe handled via `BackHandler`).
     - Screen horizontal swipe-right gesture on the upper header area.
   - **Title:** Equipment name in prominent 28 sp bold text.
   - **Latest Performance Card:**
     - Header: `last_entry`.
     - Date of the most recent workout set.
     - Metrics: Two large figures showing `weight` (with "kg") and `repetitions`.
     - If no entries exist: Displays `no_training_data`.
   - **Chart Toggle Button:** Allows displaying or hiding the visual progress chart (`UC07`).
   - **History Section:**
     - Header: `history`.
     - Reverse-chronological list of workout entries showing date, weight in kg, and repetitions.
     - If no entries exist: Displays placeholder text (`entries_here`).
4. The user can tap the Floating Action Button (`+`) to add a new workout entry (`UC05`).
5. The user can swipe any history row to reveal the delete action (`UC06`).
6. The user swipes back (or performs Android back gesture) to return to `OverviewScreen`.

---

## 5. Acceptance Criteria
- [ ] No static text link is shown for back navigation.
- [ ] Android system back gesture (edge swipe) seamlessly returns the user to the overview screen via `BackHandler`.
- [ ] Horizontal swipe-to-right gesture on the detail header section navigates back.
- [ ] The "Latest Entry" card automatically reflects the highest date entry.
- [ ] History list is sorted in descending chronological order (newest on top).
- [ ] Number formatting adheres to locale standards (e.g., German decimal comma).
