package ru.handh.apigateway.dto.request

data class RegisterRequest(
    val name: String?,
    val username: String?,
    val password: String?,
    val confirmPassword: String?
)
