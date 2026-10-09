# User Stories: Hidden Pitch (v1)

Format: As a [user], I want to [action], so that [goal]. Each story lists acceptance criteria (AC) and the SRS requirements it covers.

---

## Epic A: Browsing and Finding Places

### US-1: Browse places without an account
As a **visitor**, I want to see approved places without signing in, so that I can start using the app immediately.
- AC1: Opening the app shows a list of approved places with no login screen.
- AC2: Pending and rejected places never appear.
- AC3: The list loads in under 2 seconds on a normal mobile connection and is paginated.
*Covers: FR-1, FR-4, NFR-3*

### US-2: Search by city or place name
As a **visitor from another city**, I want to search by city or place name, so that I can find places where I am going to be.
- AC1: A search box accepts Arabic or English text.
- AC2: Results match city name or place name (partial match is fine).
- AC3: If nothing matches, an "No places found" message is shown.
*Covers: FR-2*

### US-3: Filter places
As a **visitor**, I want to filter by sport, free/paid, audience and lighting, so that I only see places that suit me.
- AC1: Filters available: sport type, free/paid, audience (everyone / boys-men / girls-women), lit at night.
- AC2: Filters can be combined with each other and with search.
- AC3: A "Clear filters" action resets everything.
- AC4: An audience filter of "girls-women" also shows places marked for everyone.
*Covers: FR-3*

### US-4: View place details
As a **visitor**, I want to open a place and see its details, so that I know if it is worth going.
- AC1: The page shows name, city, address, sports, audience, free/paid and 2-3 photos.
- AC2: Optional fields (price, hours, surface, lit, phone, Maps link) appear only if provided; otherwise they are hidden, not shown as empty.
- AC3: Photos can be swiped and opened full screen.
*Covers: FR-5, FR-6*

### US-5: Open the place in Google Maps
As a **visitor**, I want to open the place's location in Google Maps, so that I can get directions.
- AC1: If the place has a Maps link, a "Open in Google Maps" button is shown.
- AC2: Tapping it opens the Google Maps app (or the browser if the app is missing).
- AC3: If there is no link, the button is not shown.
*Covers: FR-7*

---

## Epic B: Accounts

### US-6: Sign in with Google
As a **visitor**, I want to sign in with Google in one tap, so that I can add places without creating a password.
- AC1: Sign-in is requested only when the user taps "Add place" (or opens "My places").
- AC2: After a successful sign-in the user returns to what they were doing.
- AC3: If sign-in fails or is cancelled, the user stays in the app as a visitor and sees a clear message.
- AC4: A "Sign out" option exists.
*Covers: FR-8, FR-9*

---

## Epic C: Adding Places

### US-7: Submit a new place
As a **registered user**, I want to add a place I know, so that others can discover it.
- AC1: The form contains required fields: name, city, address, sport type(s), audience, free/paid, 2-3 photos.
- AC2: The form contains optional fields: price, opening hours, surface type, lit at night, Google Maps link, contact phone.
- AC3: The Submit button stays disabled until all required fields are valid; errors are shown next to the failing field.
- AC4: Fewer than 2 or more than 3 photos is rejected with a message.
- AC5: Photos are compressed on the device before upload.
- AC6: After submitting, the user sees "Sent for review" and the place is saved as `pending`.
- AC7: Upload failures (no connection) keep the form data and let the user retry.
*Covers: FR-10 to FR-14, NFR-4*

### US-8: Track my submissions
As a **registered user**, I want to see the status of the places I added, so that I know if they were approved.
- AC1: A "My places" screen lists the user's places with status pending / approved / rejected.
- AC2: Only the signed-in user's own places appear.
*Covers: FR-15, FR-18*

---

## Epic D: Editing and Deleting

### US-9: Request an edit
As a **registered user**, I want to edit a place I added, so that I can fix or update its information.
- AC1: An "Edit" action appears only on the user's own approved places.
- AC2: The edit form starts with the current values and follows the same validation as US-7.
- AC3: After submitting, the user sees "Changes sent for review".
- AC4: The old version stays visible to everyone until the changes are approved.
- AC5: If the changes are rejected, the old version remains and the user sees the status in "My places".
*Covers: FR-16, FR-18*

### US-10: Request deletion
As a **registered user**, I want to ask for my place to be removed, so that outdated places do not stay in the app.
- AC1: A "Delete" action appears only on the user's own places and asks for confirmation.
- AC2: After confirming, the place shows "Deletion pending" in "My places" and stays visible until approved.
- AC3: After approval, the place is removed from all lists and search.
*Covers: FR-17, FR-18*

---

## Epic E: Moderation (Owner, in the backend dashboard)

### US-11: Approve or reject new places
As the **owner**, I want to approve or reject submitted places, so that only real places appear.
- AC1: I can see all `pending` places with their photos and details in the backend dashboard.
- AC2: Changing status to `approved` makes the place visible in the app; `rejected` keeps it hidden.
*Covers: FR-19, FR-20*

### US-12: Approve or reject edits and deletions
As the **owner**, I want to review edit and delete requests, so that changes are controlled.
- AC1: Pending edit and delete requests are listed separately from new places.
- AC2: Approving an edit replaces the live data; approving a delete removes the place.
- AC3: Rejecting leaves the live place unchanged.
*Covers: FR-19, FR-20*

---

## Epic F: Language

### US-13: Use the app in Arabic or English
As a **user**, I want to use the app in my language, so that I understand everything.
- AC1: The app follows the device language by default (Arabic or English).
- AC2: A language switch in settings changes the language without reinstalling.
- AC3: Arabic shows a fully mirrored right-to-left layout (navigation, lists, forms, icons that imply direction).
- AC4: Place content is shown exactly as the submitter wrote it; the app does not auto-translate it.
*Covers: FR-21, FR-22*

---

## Story Summary
| ID | Story | Priority |
|---|---|---|
| US-1 | Browse without account | Must |
| US-2 | Search | Must |
| US-3 | Filter | Must |
| US-4 | Place details | Must |
| US-5 | Open in Google Maps | Should |
| US-6 | Google sign-in | Must |
| US-7 | Submit place | Must |
| US-8 | My places and status | Must |
| US-9 | Request edit | Should |
| US-10 | Request delete | Should |
| US-11 | Approve/reject new places | Must |
| US-12 | Approve/reject edits and deletes | Should |
| US-13 | Arabic/English + RTL | Must |

**Suggested build order:** US-13 setup, US-6, US-7, US-11, US-1, US-4, US-2, US-3, US-5, US-8, US-9, US-10, US-12.
