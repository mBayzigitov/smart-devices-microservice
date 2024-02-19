package ru.handh.userservice.dto

import java.util.UUID

data class JwtResponse(
    val accessToken: String,
    val refreshToken: UUID,
    val ttl: Long
)