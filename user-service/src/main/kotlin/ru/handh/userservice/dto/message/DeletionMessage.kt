package ru.handh.userservice.dto.message

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
    USER_DELETED
}
