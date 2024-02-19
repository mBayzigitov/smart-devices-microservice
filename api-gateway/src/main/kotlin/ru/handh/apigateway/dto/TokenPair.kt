package ru.handh.apigateway.dto

import java.util.UUID

data class TokenPair(
    val accessToken: String,
    val refreshToken: UUID,
    val ttl: Long
)