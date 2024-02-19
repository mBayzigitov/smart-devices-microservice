package ru.handh.apigateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity

@Configuration
class SecurityConfig {

    @Bean
    fun disableAuth(http: HttpSecurity) =
        http.csrf().disable()
            .authorizeRequests()
            .anyRequest()
            .permitAll()
            .and()
            .build()

}
