package ru.handh.deviceservice.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import ru.handh.deviceservice.dto.message.DeletionMessage

@Configuration
class KafkaConfig(
    @Value(value = "\${spring.kafka.bootstrap-servers}")
    private val bootstrapAddress: String,

    @Value("\${spring.application.name}")
    private val appName: String
) {

    @Bean
    fun kafkaDomainEventConsumerFactory(): ConsumerFactory<String, DeletionMessage> {
        val props = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
            ConsumerConfig.GROUP_ID_CONFIG to appName,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to ErrorHandlingDeserializer::class.java,
            ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS to DeletionMessageDeserializer::class.java,
        )
        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun kafkaDomainEventListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, DeletionMessage> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, DeletionMessage>()
        factory.consumerFactory = kafkaDomainEventConsumerFactory()
        return factory
    }
}