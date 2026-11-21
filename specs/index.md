# FitProgress Specification & Use-Case Catalog

## Overview
FitProgress is a lightweight, modern Android fitness tracking application built with Jetpack Compose. It allows athletes and gym-goers to manage exercise machines and track their workout progression (weight and repetitions) with visual charts, multi-language support, and privacy-focused local persistence.

---

## Architecture & Component Map

The application follows clean architectural separation between Domain Models, Data Repositories, and the Jetpack Compose UI layer:

- **Root & Navigation:** `de.ronnyfriedland.ui.FitProgressApp`, `MainActivity`
- **Domain Models:** `de.ronnyfriedland.model.Machine`, `de.ronnyfriedland.model.Entry`
- **Data Layer:** `de.ronnyfriedland.data.MachineRepository`, `de.ronnyfriedland.data.EntryRepository`, `de.ronnyfriedland.data.ImageStorage`
- **UI Screens & Components:** `OverviewScreen`, `DetailScreen`, `MachineTile`, `ChartCard`, `MachineDialog`, `EntryDialog`
- **Theme & Utilities:** `FitProgressTheme`, `Localization`

---

## Specification Documents

| Document | Topic | Description |
| :--- | :--- | :--- |
| [UC01: Machine Overview](uc01-machine-overview.md) | Equipment Management | Grid view of equipment with thumbnails, stats, daily checkmark badge, and add action. |
| [UC02: Add Machine](uc02-add-machine.md) | Equipment Management | Creating equipment with name validation and optional photo. |
| [UC03: Delete Machine](uc03-delete-machine.md) | Equipment Management | Removing equipment via long-press and cascading data deletion. |
| [UC04: Machine Details](uc04-machine-details.md) | Workout Tracking | Equipment detail view with photo viewer, latest metrics (incl. rounds), and chronological history. |
| [UC05: Add Workout Entry](uc05-add-workout-entry.md) | Workout Tracking | Logging workout set with weight, repetitions, and rounds validation. |
| [UC06: Delete Workout Entry](uc06-delete-workout-entry.md) | Workout Tracking | Gesture-based swipe-to-reveal removal of single entries. |
| [UC07: Progress Chart](uc07-progress-chart.md) | Visual Analytics | Single daily volume bar (weight × reps × rounds), last 10 entries. |
| [UC08: Localization & i18n](uc08-localization-i18n.md) | Core Services | Locale detection for DE, EN, ES with automatic English fallback. |
| [UC09: Data Persistence](uc09-data-persistence.md) | Core Services | Repository pattern, SharedPreferences JSON, and data migration. |
| [UC10: Image Storage](uc10-image-storage.md) | Core Services | Internal file system image saving, URI stream copy, and decoding. |
| [UC11: Theming & Styling](uc11-theming-and-styling.md) | UI/UX & Design | Dark theme, high-contrast inputs, custom vector graphics, and brand icon. |
