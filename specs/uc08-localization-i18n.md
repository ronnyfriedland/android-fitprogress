# UC08: Localization & Multi-Language Support (i18n)

## 1. Description
The application supports multiple languages dynamically based on device settings, reading string resources from localized property files in `app/src/main/assets/` without any hardcoded translation dictionaries in code.

---

## 2. Supported Locales
- **German (`de`):** `assets/i18n_de.properties`
- **English (`en`):** `assets/i18n_en.properties` (Default & Fallback)
- **Spanish (`es`):** `assets/i18n_es.properties`

---

## 3. Resolution Mechanism
The `localized(context: Context, key: String): String` composable function executes the following algorithm:

1. Detects device system language via `Locale.getDefault().language.lowercase(Locale.ROOT)`.
2. Matches against supported languages (`de`, `es`); if unlisted, defaults to `en`.
3. Loads and caches the language property file `i18n_{lang}.properties` using Jetpack Compose's `remember(lang)`.
4. If the active language is not English, it loads `i18n_en.properties` as the fallback parent defaults (`Properties(defaultProps)`).
5. Resolves the requested key:
   - If key exists in active language, returns translation.
   - If key is missing in active language, returns English translation.
   - If key is missing in both, returns the raw key identifier as safeguard.

---

## 4. Key Catalog
All translation files maintain a matching set of keys:
- Navigation & Headers: `your_training`, `machines_count`, `back`, `last_entry`, `history`, `entry_title`
- Labels & Fields: `weight`, `repetitions`, `date`, `weight_kg`, `reps`, `reps_unit`, `machine_name`, `own_image`
- Actions: `upload_image`, `remove_image`, `save`, `cancel`, `delete`, `yes`, `no`, `show_chart`, `hide_chart`
- Chart: `chart_title`, `chart_weight`, `chart_reps`, `chart_scale_note`
- Prompts & Validation: `no_machines`, `add_first`, `no_entries`, `no_training_data`, `entries_here`, `please_name`, `delete_machine_title`, `delete_machine_message`

---

## 5. Acceptance Criteria
- [ ] No hardcoded translation maps exist in Kotlin source code.
- [ ] Switching system language dynamically updates UI strings.
- [ ] Any unsupported language automatically resolves to English.
- [ ] Format specifiers (e.g. `%1$d`, `%1$s`) work correctly with `String.format(locale, ...)`.
