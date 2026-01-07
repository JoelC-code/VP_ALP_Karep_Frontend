

4. Register menggunakan `full_name` tapi backend expect `address`

## Perubahan Yang Sudah Dilakukan

### 1. LoginRequest Model (user_model.kt)
**SEBELUM:**
```kotlin
data class LoginRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String
)
```

**SESUDAH:**
```kotlin
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)
```

### 2. LoginResponse Model (user_model.kt)
**SEBELUM:**
```kotlin
data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val user: User
)
```

**SESUDAH:**
```kotlin
data class LoginResponse(
    @SerializedName("data")
    val data: LoginData
)

data class LoginData(
    @SerializedName("token")
    val token: String,
    @SerializedName("account_type")
    val accountType: String
)
```

### 3. LoginViewModel (login_vm.kt)
- Update untuk mengirim `email` bukan `username`
- Update untuk handle response structure baru dengan `data` wrapper
- Error message lebih informatif

### 4. User Model (user_model.kt)
**SEBELUM:**
```kotlin
@SerializedName("full_name")
val fullName: String? = null,
```

**SESUDAH:**
```kotlin
@SerializedName("name")
val fullName: String? = null,
```

### 5. UpdateProfileRequest (user_model.kt)
**SEBELUM:**
```kotlin
@SerializedName("full_name")
val fullName: String? = null,
```

**SESUDAH:**
```kotlin
@SerializedName("name")
val fullName: String? = null,
```

### 6. RegisterRequest (user_model.kt)
**SEBELUM:**
```kotlin
@SerializedName("full_name")
val fullName: String? = null,
```

**SESUDAH:**
```kotlin
@SerializedName("address")
val address: String? = null,
```

### 7. RegisterUiState (uistates.kt)
**SEBELUM:**
```kotlin
val fullName: String = "",
```

**SESUDAH:**
```kotlin
val address: String = "",
```

### 8. RegisterViewModel (register_vm.kt)
- Update `updateFullName()` untuk set `address` field
- Update register() untuk kirim `address` bukan `fullName`

### 9. RegisterView (register_view.kt)
- Update field label dari "Nama Lengkap" jadi "Alamat"
- Update binding dari `uiState.fullName` jadi `uiState.address`

## Testing
1. **Login**: Test dengan email "anggatest@gmail.com" dan password "Angga123"
2. **Register**: Test dengan data lengkap termasuk address
3. **Update Profile**: Test update bio, name, address, phone_number

## Backend API yang Digunakan
- Login: `POST http://10.0.163.64:3000/api/login`
  - Input: `{email, password}`
  - Output: `{data: {token, account_type}}`

- Register: `POST http://10.0.163.64:3000/api/register`
  - Input: `{username, email, password, address?, phone_number?}`
  
- Update Profile: `PUT http://10.0.163.64:3000/api/users/profile`
  - Headers: `Authorization: Bearer {token}`
  - Input: `{name?, email?, address?, phone_number?, bio?}`

## Next Steps
1. Build & Run aplikasi
2. Test login dengan credentials yang benar
3. Test register user baru
4. Test update profile termasuk bio field


