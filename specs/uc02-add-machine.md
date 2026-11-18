# UC02: Add Machine

## 1. Description
The user creates a new workout machine by entering a name and optionally selecting or taking a photo. Upon saving, the machine is persisted and immediately available on the overview grid.

---

## 2. Actors
- Mobile App User

---

## 3. Preconditions
- The user is on the main overview screen.

---

## 4. Main Flow
1. The user taps the `+` Floating Action Button on the overview screen.
2. The system displays the `MachineDialog` with:
   - Title (`add_machine`).
   - Single-line text input for equipment name (`machine_name`).
   - Image placeholder button (`upload_image`) or photo preview.
   - Cancel and Save action buttons.
3. The user enters a non-blank name.
4. Optional image selection:
   - The user taps the image area.
   - System opens a unified chooser offering Camera (`IMAGE_CAPTURE`) and Gallery (`ACTION_GET_CONTENT` with `image/*`).
   - The user selects a picture or takes a photo.
   - `ImageStorage` saves the photo to internal app storage and returns the local file path.
   - The dialog displays the image thumbnail and shows a "Remove image" button.
5. The user taps "Save".
6. The system validates the input:
   - If the name is blank, displays an error message (`please_name`).
   - If valid, calls `MachineRepository.addMachine(name.trim(), imagePath)`.
7. The dialog closes, and the new machine appears in the overview grid.

---

## 5. Alternative & Exception Flows
- **User Cancels:** The user taps "Cancel" or outside the dialog. No changes are saved.
- **Empty Name Submission:** If the name is empty or consists only of whitespace, the text field indicates an error state and saving is blocked.
- **Image Removal:** The user taps "Remove image". The internal file path is reset to empty string, and the placeholder is restored.
- **Camera/Gallery Aborted:** If the user cancels the picker, the existing selection remains unchanged.

---

## 6. Acceptance Criteria
- [ ] Equipment cannot be saved with an empty name.
- [ ] Machine name is automatically trimmed of leading and trailing whitespace.
- [ ] Camera and Gallery intents are correctly invoked via `ActivityResultContracts.StartActivityForResult`.
- [ ] Selected photos are saved inside the app-internal `filesDir` (never in external cache).
- [ ] Newly added machines immediately appear in the UI without requiring an app restart.
