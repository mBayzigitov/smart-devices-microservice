package ru.handh.homeservice.kafka.producer

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.handh.homeservice.repository.OutboxMessageRepository
import javax.transaction.Transactional

@Component
class OutboxMessageSender(
    private val outboxMessageRepository: OutboxMessageRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    @Scheduled(cron = "* * * * * *") // every second
    @Transactional
    fun sendMessagesToBroker() {
        val sentMessageIds = outboxMessageRepository.findAll().map {
            kafkaTemplate.send(it.topic, it.data)
            it.id
        }
        outboxMessageRepository.deleteAllById(sentMessageIds)
    }

}