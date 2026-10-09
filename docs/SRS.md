# Software Requirements Specification: Hidden Pitch (v1)

## 1. Purpose
Hidden Pitch is a lightweight Android app that helps people, especially visitors and newcomers, find hidden or little-known places to play sports (football pitches, padel and tennis courts, playgrounds, and similar). Places are seeded by the owner and crowdsourced from registered users, with every submission approved manually before it is shown.

**Launch area:** Kafr el-Sheikh (one city).
**App name:** Hidden Pitch.

## 2. Scope
**In scope (v1):** browsing, searching and filtering places; Google sign-in; submitting, editing and deleting own places (with approval); Arabic and English with RTL.

**Out of scope (v1):** in-app map, GPS and sort-by-distance, booking, finding other players, chat, ratings and reviews, "report this place" button, admin screens or admin web page, iOS.

## 3. Users
| User | Description | Login needed |
|---|---|---|
| Visitor | Anyone browsing places | No |
| Registered user | Signed in with Google; can submit, edit and delete own places | Yes |
| Owner/admin | Approves or rejects submissions directly in the backend dashboard (no app UI) | Backend access |

## 4. Functional Requirements

### 4.1 Browsing and search
- **FR-1** Any visitor can browse approved places without an account.
- **FR-2** Users can search by city name or place name.
- **FR-3** Users can filter by: sport type, free/paid, audience (everyone / boys-men / girls-women), lit at night.
- **FR-4** Only places with status `approved` are shown.

### 4.2 Place details page
- **FR-5** Each place shows: name, city, written address, sport type(s), audience, free/paid, photos (2-3).
- **FR-6** Each place shows these only if provided: price, opening hours, surface type (grass, sand, concrete, artificial turf), lit at night, contact phone, Google Maps link.
- **FR-7** If a Google Maps link exists, a button opens it in the user's Google Maps app. The app itself contains no map.

### 4.3 Accounts
- **FR-8** Users sign in with Google (one tap). No passwords.
- **FR-9** Signing in is required only to add, edit or delete places.

### 4.4 Adding a place
- **FR-10** A signed-in user can submit a new place.
- **FR-11** Required fields: name, city, written address, sport type(s), audience, free/paid, **2-3 photos**.
- **FR-12** Optional fields: price, opening hours, surface type, lit at night, Google Maps link, contact phone.
- **FR-13** The app compresses photos on the device before upload.
- **FR-14** A new submission is saved with status `pending` and is not visible to other users.
- **FR-15** The submitter can see the status of their own submissions (pending / approved / rejected).

### 4.5 Editing and deleting
- **FR-16** A user can request an edit to a place they added. The currently approved version stays visible until the owner approves the edit.
- **FR-17** A user can request deletion of a place they added. The place is removed only after the owner approves.
- **FR-18** Users can only edit or delete their own places.

### 4.6 Moderation (no admin UI in v1)
- **FR-19** The owner approves or rejects new places, edits and delete requests by changing a status field in the backend dashboard.
- **FR-20** Rejected items are never shown publicly.

### 4.7 Language
- **FR-21** The app supports Arabic and English, switchable by the user, defaulting to the device language.
- **FR-22** Arabic uses a full right-to-left layout.

## 5. Non-Functional Requirements
- **NFR-1 Lightweight:** smallest practical app size and fast startup. Native Android (Kotlin + Jetpack Compose); no map SDK and no heavy dependencies.
- **NFR-2 Platform:** Android only; target the current and two previous major Android versions at least.
- **NFR-3 Performance:** list and search results load in under 2 seconds on a normal mobile connection; paginate lists.
- **NFR-4 Storage:** photos are compressed (target under about 300 KB each) to keep storage and bandwidth low.
- **NFR-5 Security:** only the owner of a place can submit edits or deletes for it; unapproved content is not readable by other users; enforced by backend security rules, not only in the app.
- **NFR-6 Privacy:** store only what Google sign-in provides (name, email, ID). Contact phone numbers appear only if the submitter chose to provide them.
- **NFR-7 Data licensing:** places are entered from the owner's own knowledge and from user submissions. Do not bulk copy or scrape Google Maps data.

## 6. Data Model (summary)
- **User:** id, display name, email, created at.
- **Place:** id, name, city, address, sports[], audience, is_free, price?, hours?, surface?, lit?, maps_link?, phone?, photos[2-3], owner_id, status (`pending` / `approved` / `rejected`), created at.
- **PlaceChangeRequest:** id, place_id, type (`edit` / `delete`), proposed changes, status (`pending` / `approved` / `rejected`), created at.

## 7. Seed Plan
Before launch, the owner adds at least 15-20 approved places in Kafr el-Sheikh so the app is not empty on day one.

## 8. Later List (post-v1)
1. Admin panel (web page) for approvals
2. In-app map and GPS, sort by distance
3. Ratings, reviews, "report this place"
4. Booking and finding players
5. More cities
6. iOS

## 9. Open Items for the Architecture Step
- Choose backend: Firebase or Supabase (both support Google sign-in, photo storage and a dashboard for approvals).
- Decide how edit requests are stored and shown in the dashboard (separate table recommended).
- Define the list of sport types and cities shown in filters.
- Confirm the minimum Android version.
