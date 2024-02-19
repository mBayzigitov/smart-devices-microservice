package ru.handh.homeservice.service.container

import org.junit.ClassRule
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

class PostgresTestContainer {

    companion object {

        @JvmField
        @ClassRule
        val postgresqlContainer: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa")
            .also { it.start() }
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=${postgresqlContainer.jdbcUrl}",
                "spring.datasource.username=${postgresqlContainer.username}",
                "spring.datasource.password=${postgresqlContainer.password}"
            ).applyTo(configurableApplicationContext.environment)
        }
    }
}