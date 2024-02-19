package ru.handh.homeservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HomeServiceApplication

fun main(args: Array<String>) {
    runApplication<HomeServiceApplication>(*args)
}
