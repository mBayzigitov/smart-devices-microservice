package ru.handh.userservice.dto.message

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("USER_DELETED")
data class UserDeletionMessage(
    val deletedUserId: Int
) : DeletionMessage(messageType = DeletionType.USER_DELETED)