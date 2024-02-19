package ru.handh.homeservice.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.kafka.common.serialization.Deserializer
import ru.handh.homeservice.dto.message.DeletionMessage
import ru.handh.homeservice.dto.message.UserDeletionMessage

class DeletionMessageDeserializer : Deserializer<DeletionMessage> {

    private val mapper = jacksonObjectMapper()

    init {
        mapper.registerSubtypes(UserDeletionMessage::class.java)
    }

    override fun deserialize(topic: String,
                             data: ByteArray): DeletionMessage {
        return mapper.readValue(data, DeletionMessage::class.java)
    }

}