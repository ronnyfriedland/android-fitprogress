# UC10: Image Storage & Media Management

## 1. Description
The `ImageStorage` component handles capturing, importing, compressing, and caching machine photos within the sandboxed internal storage directory of the application.

---

## 2. Technical Capabilities

### 2.1 URI Stream Import (`copyImageToAppStorage`)
- Used when images are selected from the device Gallery via `ACTION_GET_CONTENT`.
- Opens an input stream from `contentResolver.openInputStream(uri)`.
- Generates a timestamped target file in `context.filesDir`: `machine_{timestamp}.jpg`.
- Copies stream bytes using `input.copyTo(output)` and returns the absolute path.

### 2.2 Bitmap Compression & Save (`saveBitmapToAppStorage`)
- Used when a direct thumbnail bitmap is returned from Camera intent (`IMAGE_CAPTURE`).
- Compresses the bitmap to JPEG at 90% quality: `bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)`.
- Saves to `context.filesDir/machine_{timestamp}.jpg` and returns the absolute path.

### 2.3 Bitmap Decoding (`loadImageBitmap`)
- Safely decodes an image path into a `Bitmap` using `BitmapFactory.decodeFile(path)`.
- Validates non-blank paths and handles missing or corrupt files gracefully by returning `null`.

---

## 3. Security & Sandboxing
- Photos are stored strictly inside the application's internal files directory (`/data/user/0/<package>/files/`).
- No external storage permissions (`READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE`) are needed on modern Android versions.
- Photos are private to FitProgress and protected from unauthorized access by other apps.

---

## 4. Acceptance Criteria
- [ ] Image paths are saved as absolute strings in the machine record.
- [ ] Memory-safe decoding with null-safety when image files are missing or deleted.
- [ ] JPEG compression maintains a balanced trade-off between visual clarity and low storage footprint.
