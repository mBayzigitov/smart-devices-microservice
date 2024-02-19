package ru.handh.apigateway.dto

data class TokensDto(
    val accessToken: String?,
    val refreshToken: String?
)
