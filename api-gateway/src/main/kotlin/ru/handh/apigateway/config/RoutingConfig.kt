package ru.handh.apigateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RoutingConfig {

    @Bean
    fun restTemplateBean() =
        RestTemplate()

}