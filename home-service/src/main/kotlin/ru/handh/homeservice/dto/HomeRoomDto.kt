package ru.handh.homeservice.dto

data class HomeRoomDto(
    val homeId: Int,
    val roomId: Int,
    val userId: Int
) {

    fun toUserIdDto() =
        UserIdDto(userId)

}
