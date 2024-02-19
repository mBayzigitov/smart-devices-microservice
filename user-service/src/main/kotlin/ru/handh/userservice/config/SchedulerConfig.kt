package ru.handh.userservice.config

import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import javax.sql.DataSource

@Configuration
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
@ConditionalOnProperty(name = ["schedulers.enabled"], havingValue = "true")
class SchedulerConfig {
    @Bean
    fun lockProvider(dataSource: DataSource) =
        JdbcTemplateLockProvider(dataSource)
}