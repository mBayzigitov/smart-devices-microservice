package ru.handh.apigateway.dto

data class TokenInfoDto(
    val userId: Int,
    val username: String,
    val uuid: String
)
