package ru.handh.homeservice.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import ru.handh.homeservice.dto.message.DeletionMessage

@Configuration
class KafkaConfig(
    @Value(value = "\${spring.kafka.bootstrap-servers}")
    private val bootstrapAddress: String,

    @Value("\${spring.application.name}")
    private val appName: String
) {

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        val configProps: Map<String, Any> = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapAddress,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        )

        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(producerFactory())
    }

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