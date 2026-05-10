# TWDist — Android

This repository contains the **Android client** for the TWDist app (a to-do / project management app). It is built with **Kotlin**, **Jetpack Compose** (Material 3), **Hilt** for dependency injection, and **Retrofit** + **kotlinx.serialization** for the HTTP API.

## Requirements

- [Android Studio](https://developer.android.com/studio) (recommended) or a local JDK 11+ and Android SDK
- **minSdk 24**, **targetSdk / compileSdk 36** (see `app/build.gradle.kts`)

## Installation and configuration

1. Clone the repository and open the project root in Android Studio (or enter the folder in a terminal):

```sh
git clone https://github.com/DavMunHer/TWDist-Android.git
cd TWDist-Android
```

*(If the remote URL differs, use your fork or team repository instead.)*

2. Let Gradle download dependencies — in Android Studio use **File → Sync Project with Gradle Files**, or from the project root:

```sh
./gradlew.bat help
```

On macOS or Linux:

```sh
./gradlew help
```

3. **API base URL (optional)**  
   The app reads `BASE_URL` from a `.env` file in the **project root** at build time. If the file is missing, the default is `http://10.0.2.2:8080/api/` (suitable for an emulator talking to a server on the host machine).

   Example `.env`:

```properties
BASE_URL=https://your-api.example.com/api/
```

## Start developing

- **Android Studio:** run the **app** configuration on an emulator or device.
- **Command line:** assemble or install a debug build:

```sh
./gradlew.bat assembleDebug
./gradlew.bat installDebug
```

*(Use `./gradlew` instead of `./gradlew.bat` on Unix-like systems.)*

## Testing

Unit tests live under `app/src/test/java` (JUnit, MockK, coroutines test utilities, and Robolectric where needed). Instrumented UI tests are under `app/src/androidTest/java`.

Useful commands:

```sh
./gradlew.bat test
./gradlew.bat lint
./gradlew.bat assembleDebug
```

---

## Data layer — Room SQLite cache (Offline First)

### Overview

All persistent local state is stored in a **Room SQLite database** (`TWDistDatabase`). The database acts as the **single source of truth** for the UI. There are no in-memory stores or cross-screen event buses.

### Entities and relationships

| Entity          | Table     | Key relationships                                    |
|-----------------|-----------|------------------------------------------------------|
| `ProjectEntity` | `project` | Primary key `id`; shared across Explore and Project Details |
| `SectionEntity` | `section` | FK → `project.id` (CASCADE delete), indexed          |
| `TaskEntity`    | `task`    | FK → `section.id` (CASCADE delete), indexed          |

`Section.taskIds` and `Project.sectionIds` are derived from the FK relationships at query time rather than being stored as columns.

### How repositories work (write-through cache)

Every repository implementation (`ProjectRepositoryImpl`, `SectionRepositoryImpl`, etc.) follows the same pattern:

1. Call the remote API (Retrofit).
2. On success, **upsert** the result into the relevant DAO.
3. Return the domain model to the caller.

The database is always up to date after a successful network call.

### Reactive Today and Upcoming (Offline First)

`TodayRepository` and `UpcomingRepository` expose a **`Flow<List<...>>`** backed by Room reactive queries, plus a **`suspend refreshXxx()`** method that fetches from the API and writes through to Room.

```kotlin
// Observe changes from SQLite (reactive, emits on every DB change)
getTodayTasksUseCase()       // returns Flow<List<TodayTask>>

// Pull fresh data from the API and persist it
refreshTodayTasksUseCase()   // suspend -> Result<Unit>
```

ViewModels collect the `Flow` in `init` and call `refresh` immediately — the screen shows cached data instantly, then updates when the network response arrives.

### Data flow

```
API (JSON DTOs)
      |
      v  (Mappers: DTO -> Entity + Domain)
Repository Impl -> DAO.upsert(entity)          <- write side
                         |
                   TWDistDatabase (SQLite)
                         |
                   DAO.observeXxx() Flow        <- read side (Today / Upcoming)
                         |
                   Repository.observeXxx()
                         |
                   Use Case (observe / refresh)
                         |
                   ViewModel (StateFlow) -> Compose UI
```

### Rules of thumb

1. **Domain models in the domain layer** — keep DTOs and Room entities in the data layer; map before business logic runs.
2. **UI models are derived** — presentation types (e.g., `ProjectDetailsUiState`) are built for the screen, not stored as the source of truth.
3. **Prefer use cases from ViewModels** — screens call ViewModel APIs that delegate to use cases rather than calling Retrofit or DAOs directly.
4. **Immutable updates** — prefer `copy` on data classes and clear state transitions in the ViewModel.
5. **DAOs are the local persistence boundary** — repositories own the DAO calls; use cases and ViewModels never call DAOs directly.
