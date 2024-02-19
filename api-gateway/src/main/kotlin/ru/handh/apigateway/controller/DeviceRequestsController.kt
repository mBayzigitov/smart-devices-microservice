package ru.handh.apigateway.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.handh.apigateway.dto.request.DeviceControlRequest
import ru.handh.apigateway.dto.request.DeviceInfoRequest
import ru.handh.apigateway.dto.request.DeviceInfoSimpleRequest
import ru.handh.apigateway.service.DeviceRequestsService

@RestController
@RequestMapping("/api/devices")
class DeviceRequestsController(
    private val deviceRequestsService: DeviceRequestsService
) {

    @PostMapping
    fun createDevice(@RequestHeader("X-Access-Token") accessToken: String,
                     @RequestBody deviceInfoRequest: DeviceInfoRequest) =
        deviceRequestsService.createDevice(deviceInfoRequest)

    @PutMapping("/{deviceId}")
    fun editDevice(@RequestHeader("X-Access-Token") accessToken: String,
                   @PathVariable deviceId: String,
                   @RequestBody request: DeviceInfoSimpleRequest) =
        deviceRequestsService.editDevice(
            deviceId = deviceId,
            request = request
        )

    @GetMapping("/{deviceId}")
    fun getDevice(@RequestHeader("X-Access-Token") accessToken: String,
                  @PathVariable deviceId: String) =
        deviceRequestsService.getDevice(deviceId)

    @DeleteMapping("/{deviceId}")
    fun deleteDevice(@RequestHeader("X-Access-Token") accessToken: String,
                     @PathVariable deviceId: String) =
        deviceRequestsService.deleteDevice(deviceId)

    @GetMapping
    fun getAllDevices(@RequestHeader("X-Access-Token") accessToken: String,
                      @RequestParam("homeId") homeId: Int,
                      @RequestParam("roomId", required = false) roomId: Int?) =
        deviceRequestsService.getAllDevices(
            homeId = homeId,
            roomId = roomId
        )

    @PostMapping("/{deviceId}/control")
    fun sendCommands(@RequestHeader("X-Access-Token") accessToken: String,
                     @RequestBody request: DeviceControlRequest,
                     @PathVariable deviceId: String) =
        deviceRequestsService.changeDeviceStatus(
            deviceId = deviceId,
            request = request
        )

}