package ru.handh.homeservice.dto.message

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("ROOM_DELETED")
data class RoomDeletionMessage(
    val deletedRoomId: Int
) : DeletionMessage(messageType = DeletionType.ROOM_DELETED)