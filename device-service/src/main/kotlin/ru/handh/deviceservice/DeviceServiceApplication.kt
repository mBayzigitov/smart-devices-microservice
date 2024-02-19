package ru.handh.deviceservice

import com.tuya.connector.spring.annotations.ConnectorScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@ConnectorScan(basePackages = ["ru.handh.deviceservice.connector"])
@SpringBootApplication
class DeviceServiceApplication

fun main(args: Array<String>) {
    runApplication<DeviceServiceApplication>(*args)
}
