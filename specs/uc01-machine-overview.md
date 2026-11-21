# UC01: Machine Overview

## 1. Description
The user views the main dashboard containing all registered gym machines / equipment. Each machine is represented as an interactive card in a 2-column grid layout, displaying either a custom thumbnail image or an uppercase initial fallback, along with the latest workout achievement (weight and repetitions).

---

## 2. Actors
- Mobile App User / Athlete

---

## 3. Preconditions
- The application has been launched and the root screen is displayed.

---

## 4. Main Flow
1. The user launches or navigates back to the main overview screen (`OverviewScreen`).
2. The system queries `MachineRepository.getMachines()` to retrieve all configured machines.
3. If no machines exist:
   - System displays an empty state banner with title (`no_machines`) and instructions (`add_first`).
4. If machines exist:
   - System renders a 2-column grid (`LazyVerticalGrid`).
   - For each machine, `EntryRepository.getLatestEntry(machine.id)` is resolved.
   - A `MachineTile` is displayed showing:
     - Machine image if present; otherwise a styled initial letter box.
     - Machine name (single line, ellipsized on overflow).
     - Latest workout metrics: `"{weight} kg · {reps} {reps_unit} × {rounds} {rounds_unit}"` (or `no_entries` placeholder).
    - A checkmark badge (`CheckIcon`, Canvas-drawn, accent green `0xFF8BC6A0`) in the top-end corner when the machine has at least one entry dated today.
5. The user taps a tile to open the detail view (`UC04`).
6. The user long-presses a tile to initiate machine deletion (`UC03`).
7. The user taps the floating action button (`+`) to add a new machine (`UC02`).

---

## 5. UI Elements & Layout
- **Brand Header:** Title `FITPROGRESS` in bold accent green (`0xFF28734C`) with tracking letter spacing.
- **Section Heading:** Localized title (`your_training`) and subtitle counter (`machines_count`).
- **Grid Layout:** 2 columns, fixed width, 12 dp spacing between items, bottom padding 96 dp for FAB clearance.
- **Floating Action Button (FAB):** Fixed at bottom-end with container color `Accent` and `+` icon.

---

## 6. Acceptance Criteria
- [ ] If no machines exist, the user clearly sees the empty state message.
- [ ] Equipment tiles load thumbnail images efficiently without blocking UI rendering.
- [ ] When an image is missing, the tile displays the first character of the machine name in uppercase on a dark surface background.
- [ ] The latest workout stats reflect the entry with the most recent date, including repetitions and rounds.
- [ ] A machine trained today displays a checkmark badge on its tile; machines not trained today show no badge.
- [ ] The badge uses the same Canvas-drawn vector style and accent color as the app's other custom icons.
- [ ] Tapping a card opens the correct machine detail screen.
- [ ] Long-pressing triggers the deletion confirmation dialog.
