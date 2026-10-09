# Architecture: Hidden Pitch (v1)

Companion to `SRS.md`, `user-stories.md`, `data-model.md`. Where this file changes the data model, section 3 says so and this file wins.

---

## 0. Decisions locked

| Topic | Decision |
|---|---|
| Edit / delete (Q11) | Users edit own places and request deletion. Both need owner approval. Old version stays live until approved. |
| Backend | **Supabase** (Postgres + Auth + Storage + dashboard) |
| Moderation | Owner flips a `status` cell in the Supabase table editor. No admin UI in v1. |
| Visibility | Only `approved` places are public. |
| Audience filter | `girls_women` also shows `everyone`; same for `boys_men`. |
| User content | Shown exactly as written. No translation. |
| Platform | Android only, Kotlin + Jetpack Compose, one city (Kafr el-Sheikh). |

---

## 1. Why Supabase (vs Firebase) for this project

- **Moderation fits the table editor.** Approving = change one cell. Firebase's console is clumsy for reviewing and editing documents with photos.
- **Relational data suits the filters.** Search + combined filters + pagination are simple SQL. In Firestore, combined filters need composite indexes and can't do partial text search.
- **Row-level security** enforces NFR-5 in the database itself.
- **Cost / card.** Supabase free tier needs no credit card. Firebase Cloud Storage now requires the paid Blaze plan (card on file) for new projects. Verify this before deciding if you ever reconsider.
- **Google sign-in works** through the Google ID token from Android Credential Manager.

**Free tier cautions (verify current limits on the Supabase pricing page):**
1. Free projects are **paused after about 1 week of inactivity**. Add a keep-alive (task A5).
2. Storage and egress are small on the free tier. Photos are the main cost, so the app uploads a **small thumbnail** for lists (section 3.4).
3. Pick the region closest to Egypt (a Europe region, e.g. Frankfurt). The region cannot be changed later.

Rough capacity: 3 photos at about 300 KB plus a thumbnail is about 1 MB per place. A 1 GB storage limit is about 1,000 places, plenty for one city.

---

## 2. Tech stack (lightweight on purpose)

| Layer | Choice | Notes |
|---|---|---|
| Language/UI | Kotlin, Jetpack Compose, Material 3 | |
| minSdk | **26 (Android 8.0)** | Covers almost all active phones in Egypt. SRS says "current and two previous at least"; this is a deliberate wider floor. Confirm. |
| targetSdk | Latest stable | Required by Google Play. |
| Navigation | Navigation Compose | |
| DI | **None** (manual, one `AppContainer` object) | Skip Hilt/Koin to save size and build time. |
| Backend client | `supabase-kt` (auth, postgrest, storage) | Measure release APK at end of M3. If too large, fall back to OkHttp + kotlinx.serialization calling the REST API directly. |
| Google sign-in | AndroidX Credential Manager + Google ID | ID token passed to Supabase `signInWith(IDToken)`. |
| Images (display) | Coil (Compose) | |
| Images (compress) | Own helper: decode with `inSampleSize`, max 1280 px long side, JPEG quality loop until under 300 KB | Also makes a ~400 px thumbnail under 40 KB. |
| Pagination | Manual `range(from, to)` / keyset, page size 20 | No Paging 3. |
| Local storage | DataStore (language, nothing else) | No Room in v1. |
| Release build | R8 + resource shrinking, Android App Bundle | |

**Language/RTL:** use `AppCompatDelegate.setApplicationLocales()` for the in-app switch (follows device language by default, FR-21). Use `start/end` instead of `left/right` everywhere. Declare `android:supportsRtl="true"`. Mirror direction-implying icons (`autoMirror`).

---

## 3. Changes to `data-model.md`

These fix gaps found while designing the flows.

### 3.1 Approve/reject with one cell (replaces `approve_change_request(id)` function)
A table-editor cannot call a function. Instead add a **database trigger** on `place_change_requests`: when `status` changes `pending → approved`, the trigger automatically
- `edit`: copies `proposed_data` into the `places` row and sets `updated_at`;
- `delete`: sets the place `status = 'deleted'`.

So your whole workflow stays "change one cell".

### 3.2 Add status value `deleted`
Soft delete via `rejected` (old plan) would show an approved-then-deleted place as "rejected" in My places. Allowed `places.status`: `pending | approved | rejected | deleted`. Public: `approved` only. My places hides `deleted`.

### 3.3 Photo paths must not overwrite live photos
Old path `{owner_id}/{place_id}/{n}.jpg` would overwrite the live photos the moment a user submits an edit. New path:

`{owner_id}/{place_id}/{upload_id}_{n}.jpg` and `{owner_id}/{place_id}/{upload_id}_thumb.jpg`

`upload_id` is a new uuid per submission or edit. `photos` stores the full paths; the thumbnail path is derived by convention. After an approved edit, old files are removed by hand (v1).

### 3.4 Thumbnails
Lists load `_thumb.jpg` (about 30 KB), details load the full photos. Supabase's image transformation is a paid feature, so the app makes the thumbnail on the device.

### 3.5 One pending request per place
Partial unique index on `place_change_requests(place_id) WHERE status = 'pending'`. Prevents conflicting edit + delete requests.

### 3.6 Anti-spam
A user can have at most **10 `pending` places** (RLS insert check or a trigger).

### 3.7 `is_owner` is optional
The Supabase dashboard runs with full database rights and bypasses RLS, so you do not need `users.is_owner` for v1. Regular users simply get no UPDATE/DELETE policies on `places` or `place_change_requests.status`. Keep the column only if you later add owner-only app features.

### 3.8 Maps link check
DB check constraint: `maps_link` must start with `https://maps.app.goo.gl/`, `https://goo.gl/maps/`, or `https://www.google.com/maps`. The app re-checks before opening.

### 3.9 `users` row creation
Create the profile row with a trigger on `auth.users` insert (copy name and email), not from the app.

### 3.10 Photo privacy trade-off (decision needed)
NFR-5 says unapproved content is not readable by others. Database rows are fully protected by RLS. For photos, the simplest option is a **public bucket**: files are reachable only by an unguessable uuid path, but not truly access-controlled. A private bucket with signed URLs is stricter but disables easy caching and costs more requests. **v1 default: public bucket.** Change this only if you want strict enforcement.

---

## 4. App architecture

Single module, simple layers:

```
app/src/main/java/.../hiddenpitch/
  data/        SupabaseClient, repositories (PlaceRepository, AuthRepository, ChangeRequestRepository), DTOs
  domain/      models, enums (Sport, Audience, Surface, PlaceStatus), validators
  ui/
    theme/     colors, typography, RTL helpers
    home/      list + search + filters
    detail/    place details, photo pager, full-screen viewer
    add/       add/edit form (one screen, two modes)
    myplaces/  list with statuses
    settings/  language, sign out
    common/    components, empty/error states
  util/        ImageCompressor, MapsLinkValidator, LocaleManager
  AppContainer.kt, MainActivity.kt
```

Pattern: `ViewModel` per screen, `StateFlow` UI state, repositories return `Result`. No business logic in composables.

**Screens / navigation**
- Home (list, search bar, filter sheet) → Details
- Details → full-screen photos, open Google Maps, (owner only) Edit / Request delete
- Add place (requires sign-in)
- My places (requires sign-in)
- Settings (language, sign in/out)

**Sign-in gate:** tapping Add place or My places while signed out starts Google sign-in, then returns to the intended screen (US-6 AC2).

---

## 5. Key flows

**Submit new place:** validate → compress each photo + make thumbnail → upload to storage under `{owner_id}/{place_id}/{upload_id}_*` → insert `places` row with `status = pending`. Generate `place_id` on the device so uploads can happen before the insert. On failure, keep the form state and allow retry (US-7 AC7).

**Request edit:** form prefilled with current values → upload any new photos under a new `upload_id` → insert `place_change_requests` (`type = edit`, `proposed_data` = full editable field set). Live place untouched. "Edit" shows only on the user's own `approved` place with no pending request.

**Request delete:** confirm dialog → insert request (`type = delete`). My places shows "Deletion pending". Place stays live.

**Moderation (you, dashboard):**
- New place: open `places` filtered by `status = pending`, check photos in Storage, set `approved` or `rejected`.
- Edit/delete: open `place_change_requests` filtered by `status = pending`, review `proposed_data`, set `approved` or `rejected`. The trigger does the rest.

**Search + filters (one query):**
- text: `places.name ilike %q%` OR city name (ar/en) `ilike %q%`
- sports: `sports @> {chosen}`; `is_free`; `is_lit = true`
- audience: `audience in (chosen, 'everyone')` when a specific audience is picked
- always: `status = approved`, order `created_at desc`, page size 20

Indexes: as in `data-model.md` section 7, plus the partial unique index in 3.5.

---

## 6. Security checklist

- Supabase **anon key** ships in the app. That is normal; RLS is the protection. Never ship the `service_role` key.
- RLS on every table (`places`, `place_change_requests`, `users`, `cities`) plus storage policies.
- Public read: `places` where `status = approved`, `cities` where `is_active`.
- Signed-in insert: `places` only with `owner_id = auth.uid()` and `status = 'pending'`.
- Signed-in read: own places in any status; own change requests.
- Signed-in insert: change requests only for places they own, `requested_by = auth.uid()`, `status = 'pending'`.
- Nobody except the dashboard can change `status`.
- Storage: upload only into the folder named by your own user id.
- Test RLS with a second Google account before launch (task B4).

---

## 7. Size and performance targets

- Release APK/AAB as small as practical; check size after M3 and at release.
- Cold start under 2 seconds on a mid-range phone.
- First page of the list under 2 seconds (thumbnails, page size 20).
- No map SDK, no analytics SDK, no Firebase, no image-heavy libraries beyond Coil.

---

## 8. Build order and tasks (for the coding agent)

Order follows your story order. Each milestone ends in something runnable. Do not start the next milestone until the "Done when" checks pass.

### M0: Accounts and setup (you do this, not the agent)
- **A1** Create the Supabase project (free tier, Europe region). Save the project URL and anon key.
- **A2** Google Cloud: OAuth consent screen; create a **Web** client ID (for Supabase) and an **Android** client ID (package name + SHA-1 of the debug key, later the release key).
- **A3** Supabase → Auth → Google provider: paste the Web client ID and secret.
- **A4** Create the GitHub repo.
- **A5** Keep-alive: GitHub Actions cron (every few days) pinging the Supabase REST endpoint, so the free project is not paused.
- **A6** Decide the Play Store route (see section 9).

### M1: Backend
- **B1** Write the SQL migration: tables, check constraints, indexes (including 3.5), triggers (3.1, 3.9), `cities` seed row for Kafr el-Sheikh.
- **B2** RLS policies for all tables (section 6).
- **B3** Storage bucket `place-photos` (public read) + upload policy.
- **B4** Test with two accounts in the SQL editor / REST: user A cannot read B's pending place, cannot set `status`, cannot upload into B's folder, cannot create an 11th pending place.
- **Done when:** B4 passes and flipping a change request to `approved` correctly updates or soft-deletes the place.

### M2: App skeleton + language (US-13)
- **C1** Create the project (Compose, minSdk 26), package structure, Material 3 theme.
- **C2** `strings.xml` in English and Arabic, including all enum labels (sports, audience, surface).
- **C3** In-app language switch + follows device language by default; full RTL.
- **C4** Navigation shell with placeholder screens; Supabase config from `local.properties` / BuildConfig (no keys in git).
- **Done when:** language switch flips layout RTL/LTR without reinstalling.

### M3: Sign-in (US-6)
- **D1** Credential Manager Google sign-in → Supabase session; sign out.
- **D2** Gate Add place / My places; return to the intended screen after sign-in; clear message on cancel/failure.
- **Done when:** a new Google user gets a `users` row. Check release APK size here.

### M4: Submit a place (US-7)
- **E1** `ImageCompressor` (full ≤ about 300 KB, thumbnail ≤ about 40 KB) with unit tests.
- **E2** Add-place form with validation, disabled Submit, per-field errors, 2-3 photo rule.
- **E3** Upload + insert flow with retry that keeps form data.
- **Done when:** a submitted place shows as `pending` in the dashboard with all photos.

### M5: Moderation setup (US-11)
- **F1** Write `moderation.md`: how to review pending places and requests in the dashboard, with saved filters/views.
- **F2** Seed 15-20 real Kafr el-Sheikh places by submitting them through the app with your account, then approving them. This also tests the flow.

### M6: Browse and details (US-1, US-4)
- **G1** Home list with thumbnails, pagination, loading/empty/error states.
- **G2** Details screen: required fields always, optional fields only if present, swipeable photos, full-screen viewer.
- **Done when:** a visitor with no account sees approved places only.

### M7: Search and filters (US-2, US-3)
- **H1** Search box (Arabic/English, partial match, "No places found").
- **H2** Filter sheet: sport, free/paid, audience (with the `everyone` rule), lit at night; combine with search; Clear filters.

### M8: Maps button and My places (US-5, US-8)
- **I1** "Open in Google Maps" with validation, falling back to the browser.
- **I2** My places with statuses (pending / approved / rejected / deletion pending).

### M9: Edit and delete (US-9, US-10, US-12)
- **J1** Edit mode of the add form (prefilled, new `upload_id` for new photos, inserts a change request).
- **J2** Request delete with confirmation.
- **J3** My places shows edit/delete request status.
- **Done when:** approving an edit in the dashboard updates the live place; approving a delete removes it from all lists; rejecting changes nothing.

### M10: Release
- **K1** R8/resource shrinking, signing, AAB, size check.
- **K2** Test on a low-end device and on a slow connection.
- **K3** Privacy policy page (required by Google Play for Google sign-in), store listing in Arabic and English.
- **K4** Closed test, then publish.

---

## 9. Open items

1. **minSdk 26**: confirm.
2. **Public photo bucket** (3.10): confirm.
3. **Play Store:** a developer account has a one-time fee (about $25). Personal accounts created recently must run a closed test with about 12 testers for 14 days before going to production. Verify current rules. Until then, you can share the signed APK directly with testers.
4. **Sport list:** football, basketball, volleyball, tennis, padel, playground, other. Add or remove?
5. **Arabic search:** v1 uses plain `ilike`. Letter variants (أ / ا, ة / ه) will not match each other; add normalization later if testers notice.
6. **Privacy policy hosting:** a simple GitHub Pages page is enough.

---

## 10. Rules for the coding agent

- Follow `SRS.md`, `user-stories.md`, `data-model.md` and this file, in that order of authority (this file wins where section 3 says so).
- Implement one milestone at a time; stop and report after each "Done when".
- Do not add dependencies beyond section 2 without asking.
- Never commit keys. Never use the `service_role` key in the app.
- Every user-visible string goes in `strings.xml` (en + ar). Use `start/end`, never `left/right`.
- Do not add anything from the "not in v1" lists.
