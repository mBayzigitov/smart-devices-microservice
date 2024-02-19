package ru.handh.homeservice.error

import org.springframework.http.HttpStatus

class ApiException(
    val httpStatus: HttpStatus,
    val timestamp: String,
    val code: String,
    override val message: String
) : RuntimeException()