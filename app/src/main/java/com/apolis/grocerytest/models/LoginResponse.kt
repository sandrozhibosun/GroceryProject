package com.apolis.grocerytest.models

data class LoginResponse(
    val token: String,
    val user: UserX
)