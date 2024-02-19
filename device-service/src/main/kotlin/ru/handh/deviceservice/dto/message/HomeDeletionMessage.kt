package ru.handh.deviceservice.dto.message

import com.fasterxml.jackson.annotation.JsonTypeName

@JsonTypeName("HOME_DELETED")
data class HomeDeletionMessage(
    val deletedHomeId: Int
) : DeletionMessage(messageType = DeletionType.HOME_DELETED)