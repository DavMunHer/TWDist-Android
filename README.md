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

## State handling — normalized model and separated stores

### Why normalized?

The product model is naturally a tree: **projects → sections → tasks**. Deeply nesting everything (for example `project.sections[].tasks[]`) makes updates expensive and encourages duplicated task data when the same logical task could appear in more than one screen.

This app keeps **relationships as ID lists** on the domain entities and uses **separate in-memory stores** keyed by id so each layer can update independently and stay testable.

### Architecture overview

```
┌──────────────────────────────────────────────────────────────┐
│  Explore — ProjectStateStore                                 │
│  Caches project summaries for lists (explore, favorites, …)  │
├──────────────────────────────────────────────────────────────┤
│  Project details — ProjectDetailsProjectStateStore           │
│  Owns: current Project entities for the details feature      │
├──────────────────────────────────────────────────────────────┤
│  SectionStateStore                                           │
│  Owns: Section entities (section CRUD, task id ordering)     │
├──────────────────────────────────────────────────────────────┤
│  TaskStateStore                                              │
│  Owns: Task entities (task CRUD, per-section indexes)        │
└──────────────────────────────────────────────────────────────┘
```

**Why separate stores?**

- **Single responsibility** — each store mutates one kind of entity.
- **Easier testing** — stores and use cases can be tested in isolation.
- **Clear boundaries** — repositories merge remote results into the right store; use cases orchestrate reads and writes without turning the UI into a god object.

### Entity relationships (IDs)

| Entity    | Field        | Points to                         |
|-----------|--------------|-----------------------------------|
| `Project` | `sectionIds` | ordered sections in the project   |
| `Section` | `taskIds`    | tasks belonging to that section   |

Tasks are stored in `TaskStateStore` and loaded or updated through the task repository; sections keep **references** to tasks via `taskIds`, similar in spirit to the desktop app’s normalized shape.

### How loading a project works

When a project is fetched, the aggregate is split and written into the right stores — for example `GetProjectByIdUseCase` persists the project and its sections after a successful repository call:

```kotlin
repository.getProjectById(projectId)
    .onSuccess { aggregate ->
        projectStateStore.upsert(aggregate.project)
        sectionStateStore.upsertAll(aggregate.sections)
    }
```

Task lists for sections are loaded and merged via the task repository into `TaskStateStore`, while the **Compose UI** consumes a `ProjectDetailsUiState` that includes things like `sectionItems` and a `tasksById` map so the screen can render sections and resolve tasks without holding one giant nested graph in memory.

### Data flow (simplified)

```
API (JSON DTOs)
      │
      ▼
Mappers → domain models (Project, Section, Task, …)
      │
      ▼
Repositories + use cases → ProjectStateStore / ProjectDetailsProjectStateStore /
                          SectionStateStore / TaskStateStore
      │
      ▼
ViewModels (StateFlow) → Compose UI
```

### Rules of thumb

1. **Domain models in the domain layer** — keep DTOs and API types in the data layer; map before business logic runs.
2. **UI models are derived** — presentation types (for example `ProjectDetailsUiState`) are built for the screen, not stored as the source of truth in repositories.
3. **Prefer use cases from ViewModels** — screens call ViewModel APIs that delegate to use cases rather than calling Retrofit or stores directly.
4. **Immutable updates** — prefer `copy` on data classes and clear state transitions in the ViewModel.
5. **One store per entity family** — avoid mixing section payloads into the task store or vice versa; link them with ids.

### Subtasks and the API

The backend DTOs can include nested **subtasks** for forward compatibility; the Android domain model for the main project details flow currently centers on **project / section / task**. Deeper subtask trees can follow the same normalized pattern (flat task store + parent/child ids) when fully wired through the UI.
