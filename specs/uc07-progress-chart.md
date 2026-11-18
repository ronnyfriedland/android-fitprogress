# UC07: Progress Chart

## 1. Description
The user toggles a visual bar chart on the detail screen to observe recent performance trends. The chart renders dual vertical bars per workout session: one for weight (kg) and one for repetitions, dynamically scaled relative to the maximum values in the viewed window.

---

## 2. Actors
- Mobile App User

---

## 3. Preconditions
- The user is on `DetailScreen` for a machine with at least one recorded workout entry.

---

## 4. Main Flow
1. On `DetailScreen`, the user views the toggle button:
   - If chart is hidden: displays localized text `show_chart` ("Show progress chart").
   - If chart is visible: displays localized text `hide_chart` ("Hide chart").
2. The user taps the button to toggle chart visibility.
3. When shown:
   - The system retrieves the workout entries, sorts them chronologically (oldest to newest), and selects the **last 8 entries** (`takeLast(8)`).
   - Renders `ChartCard`:
     - Card title: localized `chart_title` ("WEIGHT & REPETITIONS").
     - Legend row:
       - Green square indicator: `chart_weight` ("■ Weight (kg)").
       - Gray square indicator: `chart_reps` ("■ Repetitions").
     - **Canvas View:**
       - Computes `maxW = max(weight)` and `maxR = max(reps)` to scale bar heights independently up to 78% of the canvas height.
       - Renders side-by-side rectangles: green bar for weight, gray bar for reps.
     - Footnote: localized `chart_scale_note` explaining relative normalization.
4. The user can tap the toggle button again to collapse the chart and free up vertical screen space.

---

## 5. Acceptance Criteria
- [ ] Chart toggle button text updates dynamically between show and hide states.
- [ ] Chart displays up to the last 8 entries in chronological order from left to right.
- [ ] Weight and repetition bars are drawn side-by-side with distinct colors (Green `#8BC6A0` vs Gray `#B4C3BC`).
- [ ] Chart scales gracefully regardless of differences in magnitude between weight (e.g. 120 kg) and repetitions (e.g. 10).
