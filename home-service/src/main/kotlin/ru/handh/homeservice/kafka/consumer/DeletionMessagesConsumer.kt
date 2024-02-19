package ru.handh.homeservice.kafka.consumer

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.handh.homeservice.dto.message.DeletionMessage
import ru.handh.homeservice.dto.message.UserDeletionMessage
import ru.handh.homeservice.service.HomeService

@Component
class DeletionMessagesConsumer(
    private val homeService: HomeService
) {

    @KafkaListener(
        topics = ["user-service.deletion"],
        containerFactory = "kafkaDomainEventListenerContainerFactory"
    )
    fun consumeMessage(message: DeletionMessage) =
        when (message) {

            is UserDeletionMessage -> {
                homeService.deleteHomesByUserId(message.deletedUserId)
            }

            else -> {
                // logging
            }

        }

}