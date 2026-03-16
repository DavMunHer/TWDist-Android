# Clean Architecture – Best Practices Reference

A practical guide based on the patterns used in this project (Android + Kotlin + MVVM + Clean Architecture).

---

## 1. Layer Separation

The project is split into three layers. **Dependencies only point inward** — the domain layer never depends on data or presentation.

```
┌─────────────────────────────────────────┐
│            Presentation Layer           │
│  (ViewModels, UI State, UI Mappers)     │
├─────────────────────────────────────────┤
│              Domain Layer               │
│  (Use Cases, Models, Repository Iface)  │
├─────────────────────────────────────────┤
│               Data Layer                │
│  (Repository Impl, DTOs, API, Mappers)  │
└─────────────────────────────────────────┘
```

### Domain Layer
- Contains **business logic only** — no Android framework imports, no serialization annotations.
- Defines **repository interfaces** (contracts), not implementations.
- Contains **domain models** (pure Kotlin data classes / value classes).
- Contains **use cases** that encapsulate a single business action.

### Data Layer
- **Implements** the repository interfaces defined in the domain.
- Contains **DTOs** (Data Transfer Objects) — raw representations of API/DB responses with serialization annotations.
- Contains **mappers** to convert DTOs ↔ domain models.
- Owns all network (Retrofit), database (Room), and external service logic.

### Presentation Layer
- Contains **ViewModels**, **UI state models**, and **UI mappers**.
- Never constructs domain models directly — uses mappers for that.
- Consumes `Result` or sealed classes from the domain — never catches raw exceptions from lower layers.

---

## 2. The Dependency Rule

> Source code dependencies must point **inward**, toward higher-level policies (the domain).

| Layer        | Can depend on       | Must NOT depend on   |
|--------------|---------------------|----------------------|
| Presentation | Domain              | Data                 |
| Domain       | Nothing (self-only) | Data, Presentation   |
| Data         | Domain              | Presentation         |

**Violation example:**
```kotlin
// ❌ Domain layer importing a DTO (data layer class)
import com.example.app.features.auth.data.dto.RegisterRequestDto
```

**Correct approach:**
```kotlin
// ✅ Domain defines its own model
data class RegisterCredentials(val email: Email, val username: Username, val password: Password)
```

---

## 3. Domain Models vs DTOs

These are **separate classes** even if they look similar. They serve different purposes:

| Aspect             | Domain Model                         | DTO                                  |
|--------------------|--------------------------------------|--------------------------------------|
| **Layer**          | Domain                               | Data                                 |
| **Purpose**        | Business logic representation        | Raw network/DB serialization         |
| **Annotations**    | None                                 | `@Serializable`, `@Entity`, etc.     |
| **Dependencies**   | None (pure Kotlin)                   | Framework-specific (kotlinx, Room)   |
| **Can evolve**     | Independently of API changes         | Tied to API/DB schema                |

**Example:**

```kotlin
// Data layer — tied to what the server sends
@Serializable
data class UserResponseDto(val id: Long, val username: String, val email: String)

// Domain layer — what the business logic works with
data class RegisteredUser(val id: Long, val username: String, val email: String)
```

They are bridged by a **mapper** in the data layer:

```kotlin
fun UserResponseDto.toDomain(): RegisteredUser {
    return RegisteredUser(id = id, username = username, email = email)
}
```

---

## 4. Mappers

Mappers are the **bridge between layers**. Each layer boundary should have its own mappers.

### Data Mappers (Data ↔ Domain)
- Live in the **data layer** (`data/mapper/`).
- Convert DTOs → domain models (response) and domain models → DTOs (request).

```kotlin
// DTO -> Domain (response)
fun UserResponseDto.toDomain(): RegisteredUser { ... }

// Domain -> DTO (request)
fun RegisterCredentials.toDto(): RegisterRequestDto { ... }
fun LoginCredentials.toDto(): LoginRequestDto { ... }
```

### Presentation Mappers (Presentation → Domain)
- Live in the **presentation layer** (`presentation/mapper/`).
- Convert UI state / form data → domain models.
- The ViewModel should **never** construct domain models directly.

```kotlin
// UI State → Domain Credentials
fun RegisterFormState.toCredentials(): Result<RegisterCredentials> { ... }

// Domain validation error → UI string
fun Result<Email>.toUiError(): String? { ... }
```

---

## 5. Repository Pattern

The **interface** lives in the domain. The **implementation** lives in the data layer.

```kotlin
// Domain layer — defines WHAT can be done
interface AuthRepository {
    suspend fun register(credentials: RegisterCredentials): Result<RegisteredUser>
    suspend fun sendLogin(credentials: LoginCredentials)
}

// Data layer — defines HOW it's done
class AuthRepositoryImpl(
    private val api: AuthApi,
    private val json: Json
) : AuthRepository {
    override suspend fun register(credentials: RegisterCredentials): Result<RegisteredUser> {
        return runCatching {
            // network call, DTO mapping, error handling
        }
    }
}
```

**Why?**
- The domain defines the contract without knowing about Retrofit, Room, or any framework.
- You can swap implementations (e.g., fake repository for tests) without touching domain or presentation.

---

## 6. Use Cases

Each use case represents **one business action**. It follows the Single Responsibility Principle.

### Rules:
- One public method: `suspend operator fun invoke(...)`.
- Receives **domain models** as parameters.
- Returns `Result<T>` to communicate success/failure through the type system.
- Delegates to the repository — does not contain network or DB logic.
- May add domain validation before calling the repository.

```kotlin
class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(credentials: RegisterCredentials): Result<RegisteredUser> {
        return authRepository.register(credentials)
    }
}
```

### Why `Result<T>` instead of throwing?
- Exceptions are for **exceptional, unexpected** situations — not for expected business errors.
- `Result` makes error handling **explicit** at the call site.
- The ViewModel uses `.onSuccess {}` / `.onFailure {}` instead of try/catch.

---

## 7. Domain Validation with Value Classes

Use **value classes** (inline classes) to enforce validity at creation time. An invalid `Email` or `Password` simply cannot exist.

```kotlin
@JvmInline
value class Email private constructor(val value: String) {
    companion object {
        fun create(raw: String): Result<Email> =
            when {
                raw.isBlank() -> Result.failure(EmailException(EmailError.Blank))
                !raw.contains("@") -> Result.failure(EmailException(EmailError.InvalidFormat))
                else -> Result.success(Email(raw))
            }
    }
    fun asString(): String = value
}
```

**Benefits:**
- The constructor is `private` — you can only create one via `create()`, which validates.
- No runtime overhead — `@JvmInline` compiles to the underlying type.
- Errors are **sealed classes**, exhaustively handled via `when`.

---

## 8. ViewModel Best Practices

### Do:
- Expose a single `StateFlow<UiState>` for the UI to observe.
- Use **presentation mappers** to convert form data → domain models.
- Handle `Result` with `.onSuccess {}` / `.onFailure {}`.
- Map domain/network errors to user-friendly strings inside the ViewModel.

### Don't:
- ❌ Construct domain models directly in the ViewModel.
- ❌ Catch raw exceptions from use cases (use `Result` instead).
- ❌ Import data layer classes (DTOs, API interfaces).
- ❌ Put business logic in the ViewModel — delegate to use cases.

```kotlin
// ✅ Correct
state.toCredentials()            // mapper handles construction
    .onSuccess { credentials ->
        registerUseCase(credentials)
            .onSuccess { _uiState.update { it.copy(isSuccess = true) } }
            .onFailure { _uiState.update { it.copy(errorMessage = it.message) } }
    }

// ❌ Wrong
val credentials = RegisterCredentials(
    email = Email.create(state.email).getOrThrow(),  // domain model built in ViewModel
    ...
)
try {
    registerUseCase(credentials)  // raw exception handling
} catch (e: Exception) { ... }
```

---

## 9. Error Handling Strategy

Errors flow **upward** through `Result`, never as raw exceptions across layer boundaries.

```
Data Layer:
  runCatching { api.call() }  →  Result.failure(Exception("..."))
       │
Domain Layer (Use Case):
  Returns Result as-is, or adds domain validation failures
       │
Presentation Layer (ViewModel):
  .onSuccess { update UI state }
  .onFailure { map to user-friendly message }
```

### Error types at each layer:

| Layer        | Error type                         | Example                                |
|--------------|------------------------------------|----------------------------------------|
| Data         | `IOException`, `HttpException`     | Network failure, 4xx/5xx responses     |
| Domain       | Domain exceptions (sealed classes) | `EmailError.Blank`, `PasswordError.TooShort` |
| Presentation | `String` messages for the UI       | `"Email cannot be blank"`              |

---

## 10. Dependency Injection

Use Hilt modules to wire everything together without breaking layer boundaries.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides @Singleton
    fun provideAuthRepository(api: AuthApi, json: Json): AuthRepository =
        AuthRepositoryImpl(api, json)  // returns the interface type

    @Provides @Singleton
    fun provideRegisterUseCase(repo: AuthRepository): RegisterUseCase =
        RegisterUseCase(repo)
}
```

**Key points:**
- Provide **interface types**, not implementations.
- ViewModels receive use cases via `@Inject constructor`.
- Use cases receive repositories via constructor — not the other way around.

---

## 11. Testing

### Use Cases
- Mock the **repository interface** (not the implementation).
- Test that the use case calls the repository correctly.
- Test that `Result.success` and `Result.failure` are propagated correctly.

```kotlin
@Test
fun `given valid credentials, when register, then returns success`() = runTest {
    coEvery { authRepository.register(credentials) } returns Result.success(expectedUser)

    val result = registerUseCase(credentials)

    assertTrue(result.isSuccess)
    assertEquals(expectedUser, result.getOrNull())
}
```

### Domain Models (Value Classes)
- Test validation logic directly (no mocking needed).

```kotlin
@Test
fun `given blank email, when create, then returns failure`() {
    val result = Email.create("")
    assertTrue(result.isFailure)
}
```

### ViewModels
- Mock the **use case**.
- Assert that `uiState` updates correctly for success and failure scenarios.

---

## 12. Project Structure Reference

```
features/auth/
├── data/
│   ├── dto/
│   │   ├── RegisterRequestDto.kt      // What we send to the API
│   │   ├── LoginRequestDto.kt
│   │   └── UserResponseDto.kt         // What the API sends back
│   ├── mapper/
│   │   └── UserMapper.kt              // DTO ↔ Domain conversions
│   ├── remote/
│   │   └── AuthApi.kt                 // Retrofit interface
│   └── repository/
│       └── AuthRepositoryImpl.kt      // Implements domain interface
├── domain/
│   ├── model/
│   │   ├── RegisterCredentials.kt     // Input domain model
│   │   ├── RegisteredUser.kt          // Output domain model
│   │   ├── LoginCredentials.kt
│   │   └── shared/
│   │       ├── Email.kt               // Value class with validation
│   │       ├── Password.kt
│   │       └── Username.kt
│   ├── repository/
│   │   └── AuthRepository.kt          // Interface (contract)
│   └── usecases/
│       ├── RegisterUseCase.kt
│       └── LoginUseCase.kt
└── presentation/
    ├── mapper/
    │   ├── RegisterFormMapper.kt       // UI state → Domain model
    │   └── AuthValidationUiMapper.kt   // Domain error → UI string
    ├── model/
    │   └── RegisterFormState.kt        // UI state data class
    └── viewmodel/
        ├── RegisterViewModel.kt
        └── LoginViewModel.kt
```

---

## Quick Checklist

- [ ] Domain layer has **zero** imports from data or presentation
- [ ] DTOs are never exposed outside the data layer
- [ ] Repository interface is in domain, implementation is in data
- [ ] Mappers bridge every layer boundary
- [ ] Use cases have a single `invoke` operator
- [ ] `Result<T>` is used instead of raw exceptions across layers
- [ ] ViewModel never constructs domain models directly
- [ ] Value classes enforce validation at creation time
- [ ] Tests mock interfaces, not implementations

