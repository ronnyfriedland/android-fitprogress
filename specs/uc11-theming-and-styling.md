# UC11: Theming, UI/UX & Design Guidelines

## 1. Description
FitProgress features a sleek, high-contrast dark theme optimized for gym environments and low-light conditions, utilizing an athletic green/gray/black color palette and custom vector artwork.

---

## 2. Color Palette & Semantics

| Token | Hex Value | Semantic Usage |
| :--- | :--- | :--- |
| `Bg` | `#111513` | Primary screen background, status bar, and navigation bar. |
| `Surface` | `#202723` | Card container background, dialog background, top bars. |
| `Surface2` | `#29332D` | Secondary surface, button containers, image placeholders. |
| `Accent` | `#28734C` | Primary brand accent, floating action button, letter headers. |
| `TextMain` | `#F1F4F1` | High-contrast body text, headings, prominent numbers. |
| `Muted` | `#AAB5AD` | Subtitles, labels, secondary information, footnotes. |
| `MetricGreen` | `#8BC6A0` | Positive trend highlights, weight metric value, back arrow. |
| `WarningRed` | `#B3261E` / `#FF6B6B` | Delete actions, swipe-to-reveal button, destructive confirm. |

---

## 3. High-Contrast Input Fields (`readableFieldColors`)
To avoid legibility issues in dark mode, text fields use custom explicit color definitions:
- Focused text color: `#F7FAF7`
- Unfocused label color: `#D0D8D2`
- Focused border & cursor: `#9AD6AE`
- Unfocused border: `#9AA69E`
- Error supporting text: `#FF8A80`

---

## 4. App Icon & Brand Vector Artwork (`@drawable/ic_fitprogress`)
The official application icon features a high-definition flexed bicep / muscle arm vector:
- Black canvas (`#111513`) with subtle charcoal emblem circle (`#202723`) and green border (`#28734C`).
- Main muscular arm silhouette rendered in athletic emerald green (`#28734C`).
- Defined bicep peak and triceps highlights in light fitness gray (`#AAB5AD`).
- Precise anatomical separation grooves and knuckle lines in deep shadow black (`#111513`).

---

## 5. Acceptance Criteria
- [ ] Consistent dark theme applied across all screens, dialogs, and cards.
- [ ] Status bar and navigation bar match the `#111513` dark background.
- [ ] Input fields maintain WCAG AA contrast standards for dark mode.
- [ ] Vector drawables scale crisply across all screen densities without rasterization artifacts.
