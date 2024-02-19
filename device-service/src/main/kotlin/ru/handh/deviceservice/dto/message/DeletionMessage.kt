package ru.handh.deviceservice.dto.message

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    property = "messageType",
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY
)
sealed class DeletionMessage(
    val messageType: DeletionType
)

enum class DeletionType {
    HOME_DELETED,
    ROOM_DELETED
}
