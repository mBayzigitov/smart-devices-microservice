package ru.handh.userservice.dto

data class ErrorDto(
    val code: String,
    val message: String,
    val timestamp: String
)