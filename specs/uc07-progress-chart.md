# UC07: Progress Chart

## 1. Description
The user toggles a visual bar chart on the detail screen to observe recent performance trends. The chart renders a single vertical bar per day, where the value is the total training volume computed as `weight × reps × rounds` summed over all entries of that day. Bars are dynamically scaled relative to the maximum value in the viewed window.

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
   - The system retrieves the workout entries, sorts them chronologically (oldest to newest), and selects the **last 10 entries** (`takeLast(10)`).
   - For each entry, the daily value is computed as `weight × reps × rounds` (see `UC05`).
   - Entries sharing the same date are aggregated into a single value per day (summed).
   - Renders `ChartCard`:
     - Card title: localized `chart_title`.
     - **Canvas View:**
       - Computes `maxValue = max(daily volume)` to scale bar heights up to 78% of the canvas height.
       - Renders one bar per day, ordered chronologically from left to right.
     - Footnote: localized `chart_scale_note` explaining relative normalization.
4. The user can tap the toggle button again to collapse the chart and free up vertical screen space.

---

## 5. Acceptance Criteria
- [ ] Chart toggle button text updates dynamically between show and hide states.
- [ ] Chart displays up to the last 10 entries, aggregated to one bar per day.
- [ ] Each bar's value equals `weight × reps × rounds` (summed per day when multiple entries exist).
- [ ] Bars are scaled relative to the maximum daily value in the displayed window.
- [ ] Days are ordered chronologically from left to right.
- [ ] Single-series legend/label reflects the volume metric (localized keys updated accordingly).
