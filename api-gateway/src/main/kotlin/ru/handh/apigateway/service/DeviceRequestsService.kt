package ru.handh.apigateway.service

import org.springframework.stereotype.Service
import ru.handh.apigateway.client.DeviceRequestsClient
import ru.handh.apigateway.dto.Device
import ru.handh.apigateway.dto.DeviceSimple
import ru.handh.apigateway.dto.request.DeviceControlRequest
import ru.handh.apigateway.dto.request.DeviceInfoRequest
import ru.handh.apigateway.dto.request.DeviceInfoSimpleRequest

@Service
class DeviceRequestsService(
    private val deviceRequestsClient: DeviceRequestsClient,
    private val gatewayService: GatewayService
) {

    fun createDevice(deviceInfoRequest: DeviceInfoRequest): Device {
        val (_, _, homeId, roomId) = deviceInfoRequest

        gatewayService.checkDeviceRelation(
            homeId = homeId,
            roomId = roomId
        )

        return deviceRequestsClient.createDevice(deviceInfoRequest)
    }

    fun editDevice(deviceId: String,
                   request: DeviceInfoSimpleRequest): Device {
        val (_, homeId, roomId) = request

        gatewayService.checkDeviceRelation(
            homeId = homeId,
            roomId = roomId
        )

        gatewayService.checkDeviceOwner(deviceId)

        return deviceRequestsClient.editDevice(
            tuyaDeviceId = deviceId,
            request = request
        )
    }

    fun getDevice(deviceId: String): Device {
        gatewayService.checkDeviceOwner(deviceId)

        return deviceRequestsClient.getDevice(deviceId)
    }

    fun deleteDevice(deviceId: String) {
        gatewayService.checkDeviceOwner(deviceId)

        return deviceRequestsClient.deleteDevice(deviceId)
    }

    fun getAllDevices(homeId: Int,
                      roomId: Int?): List<DeviceSimple> {
        gatewayService.checkDeviceRelation(
            homeId = homeId,
            roomId = roomId
        )

        return deviceRequestsClient.getDevicesList(
            homeId = homeId,
            roomId = roomId
        )
    }

    fun changeDeviceStatus(deviceId: String,
                           request: DeviceControlRequest) {
        gatewayService.checkDeviceOwner(deviceId)

        return deviceRequestsClient.sendCommands(
            tuyaDeviceId = deviceId,
            request = request
        )
    }

}