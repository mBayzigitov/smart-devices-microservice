package ru.handh.apigateway.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ru.handh.apigateway.dto.Device
import ru.handh.apigateway.dto.DeviceRelationDto
import ru.handh.apigateway.dto.DeviceSimple
import ru.handh.apigateway.dto.request.DeviceControlRequest
import ru.handh.apigateway.dto.request.DeviceInfoRequest
import ru.handh.apigateway.dto.request.DeviceInfoSimpleRequest
import ru.handh.apigateway.error.ApiError

@Component
class DeviceRequestsClient(
    private val restTemplate: RestTemplate,

    @Value("\${deviceservice.api.path}")
    private val deviceServiceBaseUrl: String
) {

    fun createDevice(request: DeviceInfoRequest) =
        restTemplate.postForObject(
            deviceServiceBaseUrl,
            request,
            Device::class.java
        ) ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun editDevice(tuyaDeviceId: String,
                   request: DeviceInfoSimpleRequest) =
        restTemplate.exchange(
            "$deviceServiceBaseUrl/{tuyaDeviceId}",
            HttpMethod.PUT,
            HttpEntity(request),
            Device::class.java,
            tuyaDeviceId
        ).body ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun getDeviceRelation(tuyaDeviceId: String) =
        restTemplate.getForObject(
            "$deviceServiceBaseUrl/relation/{tuyaDeviceId}",
            DeviceRelationDto::class.java,
            tuyaDeviceId
        ) ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun getDevice(tuyaDeviceId: String) =
        restTemplate.getForObject(
            "$deviceServiceBaseUrl/{tuyaDeviceId}",
            Device::class.java,
            tuyaDeviceId
        ) ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun deleteDevice(tuyaDeviceId: String) =
        restTemplate.delete(
            "$deviceServiceBaseUrl/{tuyaDeviceId}",
            tuyaDeviceId
        )

    fun getDevicesList(homeId: Int,
                       roomId: Int?) =
        restTemplate.exchange(
            "$deviceServiceBaseUrl?homeId=$homeId&roomId=${roomId ?: ""}",
            HttpMethod.GET,
            null,
            Array<DeviceSimple>::class.java,
            homeId, roomId
        ).body?.toList() ?: throw ApiError.SERVICE_UNAVAILABLE.toException()

    fun sendCommands(tuyaDeviceId: String,
                     request: DeviceControlRequest) {
        restTemplate.postForLocation(
            "$deviceServiceBaseUrl/{deviceId}/control",
            request,
            tuyaDeviceId
        )
    }


}