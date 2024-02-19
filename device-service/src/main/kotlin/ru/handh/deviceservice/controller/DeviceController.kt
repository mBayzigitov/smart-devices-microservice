package ru.handh.deviceservice.controller

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.handh.deviceservice.dto.request.DeviceControlRequest
import ru.handh.deviceservice.dto.request.DeviceInfoRequest
import ru.handh.deviceservice.dto.request.DeviceInfoSimpleRequest
import ru.handh.deviceservice.service.DeviceService

@RestController
@RequestMapping("/api/devices")
class DeviceController(
    private val deviceService: DeviceService
) {

    @PostMapping
    fun createDevice(@Validated @RequestBody deviceInfoRequest: DeviceInfoRequest) =
        deviceService.createDevice(deviceInfoRequest)

    @PutMapping("/{deviceId}")
    fun editDevice(@PathVariable deviceId: String,
                   @Validated @RequestBody name: DeviceInfoSimpleRequest) =
        deviceService.editDevice(deviceId, name)

    @GetMapping("/{deviceId}")
    fun getDevice(@PathVariable deviceId: String) =
        deviceService.getDevice(deviceId)

    @DeleteMapping("/{deviceId}")
    fun deleteDevice(@PathVariable deviceId: String) =
        deviceService.deleteDevice(deviceId)

    @GetMapping
    fun getAllDevices(@RequestParam("homeId") homeId: Int,
                      @RequestParam(name = "roomId", required = false) roomId: Int?) =
        deviceService.getAllDevices(
            homeId = homeId,
            roomId = roomId
        )

    @PostMapping("/{deviceId}/control")
    fun sendCommands(@RequestBody request: DeviceControlRequest,
                     @PathVariable deviceId: String) =
        deviceService.sendCommands(deviceId, request)

    @GetMapping("/relation/{tuyaDeviceId}")
    fun getDeviceRelation(@PathVariable tuyaDeviceId: String) =
        deviceService.getDeviceRelation(tuyaDeviceId)

}