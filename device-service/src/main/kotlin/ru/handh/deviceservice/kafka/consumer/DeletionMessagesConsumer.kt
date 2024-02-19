package ru.handh.deviceservice.kafka.consumer

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.handh.deviceservice.dto.message.DeletionMessage
import ru.handh.deviceservice.dto.message.HomeDeletionMessage
import ru.handh.deviceservice.dto.message.RoomDeletionMessage
import ru.handh.deviceservice.service.DeviceService

@Component
class DeletionMessagesConsumer(
    private val deviceService: DeviceService
) {

    @KafkaListener(
        topics = ["home-service.deletion"],
        containerFactory = "kafkaDomainEventListenerContainerFactory"
    )
    fun consumeMessage(message: DeletionMessage) =
        when (message) {

            is HomeDeletionMessage -> {
                deviceService.deleteDevicesUsingHomeId(message.deletedHomeId)
                println("Devices with home id [${message.deletedHomeId}] removed")
            }

            is RoomDeletionMessage -> {
                deviceService.resetDeviceRoomId(message.deletedRoomId)
                println("roomId = null for devices with room id [${message.deletedRoomId}]")
            }

        }

}