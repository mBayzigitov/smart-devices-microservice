package ru.handh.apigateway.dto

data class ErrorDto(
    val code: String,
    val message: String,
    val timestamp: String
)