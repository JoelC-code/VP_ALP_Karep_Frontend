//TODO Hapus jika mau nyambung ke backend beneran
package com.example.vp_alp_karep_frontend.models

data class LoginFakeResponse(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Int,
    val email: String,
    val name: String,
)
