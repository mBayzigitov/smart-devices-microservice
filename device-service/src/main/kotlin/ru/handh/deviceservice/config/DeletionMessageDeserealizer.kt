package ru.handh.deviceservice.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.kafka.common.serialization.Deserializer
import ru.handh.deviceservice.dto.message.DeletionMessage
import ru.handh.deviceservice.dto.message.HomeDeletionMessage
import ru.handh.deviceservice.dto.message.RoomDeletionMessage

class DeletionMessageDeserializer : Deserializer<DeletionMessage> {

    private val mapper = jacksonObjectMapper()

    init {
        mapper.registerSubtypes(HomeDeletionMessage::class.java)
        mapper.registerSubtypes(RoomDeletionMessage::class.java)
    }

    override fun deserialize(topic: String,
                             data: ByteArray): DeletionMessage {
        return mapper.readValue(data, DeletionMessage::class.java)
    }

}