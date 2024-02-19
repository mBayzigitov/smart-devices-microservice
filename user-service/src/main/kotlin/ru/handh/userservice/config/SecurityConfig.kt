package ru.handh.userservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

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

    @Bean
    fun passwordEncoder() =
        BCryptPasswordEncoder()
}