# Data Model: Hidden Pitch (v1)

Written as Postgres tables (Supabase). The same structure maps to Firebase collections if you choose Firebase later.

**Recommendation:** use **Supabase**. Its table editor lets you approve a place by changing one cell (`status`), which is exactly your v1 moderation plan. It also supports Google sign-in, photo storage, and row-level security rules. Final choice is made in `architecture.md`.

---

## 1. Entity Overview

```
users 1 ──── * places 1 ──── * place_change_requests
                  │
                  └── * (city) ──── 1 cities
```

- A **user** owns many **places**.
- A **place** belongs to one **city**.
- A **place** can have many **change requests** (edit or delete).

Sports are a fixed list stored in the app (not a table) to keep things light.

---

## 2. Tables

### 2.1 `users` (profile created on first Google sign-in)
| Field | Type | Required | Notes |
|---|---|---|---|
| id | uuid (PK) | yes | Same as the auth user id |
| display_name | text | yes | From Google |
| email | text | yes | From Google |
| is_owner | boolean | yes | Default `false`; `true` only for you (used by security rules) |
| created_at | timestamptz | yes | Default now() |

### 2.2 `cities`
| Field | Type | Required | Notes |
|---|---|---|---|
| id | uuid (PK) | yes | |
| name_ar | text | yes | e.g. كفر الشيخ |
| name_en | text | yes | e.g. Kafr el-Sheikh |
| is_active | boolean | yes | Default `true` |

Seed v1 with one row: Kafr el-Sheikh. A table (not free text) keeps city search and filters clean and lets you add cities later without code changes.

### 2.3 `places`
| Field | Type | Required | Notes |
|---|---|---|---|
| id | uuid (PK) | yes | |
| owner_id | uuid (FK → users.id) | yes | Who submitted it |
| city_id | uuid (FK → cities.id) | yes | |
| name | text | yes | As written by the submitter |
| address | text | yes | Written address |
| sports | text[] | yes | At least 1 value from the sport list (section 4) |
| audience | text | yes | `everyone` / `boys_men` / `girls_women` |
| is_free | boolean | yes | |
| price | text | no | e.g. "50 EGP/hour"; only meaningful when `is_free = false` |
| opening_hours | text | no | Free text in v1, e.g. "4 PM - 12 AM" |
| surface | text | no | `grass` / `sand` / `concrete` / `artificial_turf` |
| is_lit | boolean | no | `null` = unknown (hidden in the app) |
| maps_link | text | no | Google Maps URL |
| phone | text | no | Hidden in the app if empty |
| photos | text[] | yes | 2-3 storage paths (check constraint: length between 2 and 3) |
| status | text | yes | `pending` / `approved` / `rejected`; default `pending` |
| created_at | timestamptz | yes | Default now() |
| updated_at | timestamptz | yes | Updated on approved edits |

### 2.4 `place_change_requests`
| Field | Type | Required | Notes |
|---|---|---|---|
| id | uuid (PK) | yes | |
| place_id | uuid (FK → places.id) | yes | |
| requested_by | uuid (FK → users.id) | yes | Must equal the place's `owner_id` |
| type | text | yes | `edit` / `delete` |
| proposed_data | jsonb | for `edit` | The full new set of editable fields (same names as in `places`) |
| status | text | yes | `pending` / `approved` / `rejected`; default `pending` |
| created_at | timestamptz | yes | Default now() |

The live place is **not touched** until you approve, so the old version stays visible (US-9 AC4).

---

## 3. Status Rules

| Item | Starts as | Visible to public when |
|---|---|---|
| New place | `pending` | `status = approved` |
| Edit request | `pending` | Live place keeps old data until approved |
| Delete request | `pending` | Place stays visible until approved |

**Approving an edit:** copy `proposed_data` into the matching `places` row, set `updated_at`, and set the request to `approved`. Doing this by hand is slow, so the architecture step will add a small database function, `approve_change_request(id)`, that does it in one click.

**Approving a delete:** set the place `status = rejected` (a soft delete, so nothing is lost and it can be restored) and the request to `approved`.

---

## 4. Fixed Lists (stored in the app)

- **Sports:** `football`, `basketball`, `volleyball`, `tennis`, `padel`, `playground`, `other`
- **Audience:** `everyone`, `boys_men`, `girls_women`
- **Surface:** `grass`, `sand`, `concrete`, `artificial_turf`

Each value has an Arabic and an English label inside the app's string resources, so the lists translate automatically. Tell me if you want to add or remove sports.

---

## 5. Access Rules (row-level security)

| Who | Table | Allowed |
|---|---|---|
| Anyone (no login) | places | Read rows where `status = approved` |
| Anyone | cities | Read active cities |
| Signed-in user | places | Insert with `owner_id = own id` and `status = pending` only |
| Signed-in user | places | Read their own places in any status (for "My places") |
| Signed-in user | place_change_requests | Insert and read for places they own |
| Signed-in user | users | Read and update only their own row |
| Owner (`is_owner = true`) | all tables | Full access (also via dashboard) |
| Nobody except owner | places.status | Cannot be changed by regular users |

These rules are enforced by the backend, not only by the app (SRS NFR-5).

---

## 6. Photo Storage

- One storage bucket: `place-photos`.
- Path format: `{owner_id}/{place_id}/{n}.jpg` (n = 1 to 3).
- Public read for approved places' photos; upload only by signed-in users into their own folder.
- Photos are compressed on the device to about 300 KB or less before upload (NFR-4).
- Rejected or deleted places: remove the photos by hand for now to save space.

---

## 7. How the Screens Use the Data

| Screen | Query |
|---|---|
| Place list | `places` where `status = approved`, newest first, paginated |
| Search | Same, plus city match (`cities.name_ar/name_en`) or `places.name` contains the text |
| Filters | `sports` contains the chosen sport; `is_free`; `audience` in (chosen, `everyone`); `is_lit` |
| Place details | One `places` row plus its city name |
| My places | `places` where `owner_id = me`, with any pending edit/delete request |

**Indexes to add:** `places(status, city_id)`, `places(owner_id)`, and a text index on `places.name` for search.

---

## 8. Example Row

```json
{
  "name": "Al-Hadiqa Mini Pitch",
  "city": "Kafr el-Sheikh",
  "address": "Behind the youth center, 2nd street",
  "sports": ["football"],
  "audience": "boys_men",
  "is_free": false,
  "price": "100 EGP/hour",
  "opening_hours": "5 PM - 11 PM",
  "surface": "artificial_turf",
  "is_lit": true,
  "maps_link": "https://maps.app.goo.gl/...",
  "phone": null,
  "photos": ["u1/p1/1.jpg", "u1/p1/2.jpg"],
  "status": "approved"
}
```

---

## 9. Not in v1 (kept out on purpose)
Ratings and reviews, favorites, reports, coordinates and GPS fields, chat and bookings, an admin users table. When the map comes later, add `latitude` and `longitude` columns to `places`.
