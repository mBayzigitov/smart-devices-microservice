package ru.handh.userservice.dto

data class TokenInfoDto(
    val userId: Int,
    val username: String,
    val uuid: String
)
