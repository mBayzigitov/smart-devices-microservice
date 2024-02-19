package ru.handh.homeservice.dto

data class Home(
    val id: Int,
    val name: String,
    val address: String?,
    val rooms: List<Room>
)